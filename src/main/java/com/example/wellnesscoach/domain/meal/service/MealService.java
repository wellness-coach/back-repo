package com.example.wellnesscoach.domain.meal.service;

import com.example.wellnesscoach.domain.chatGPT.dto.ChatGPTResponseDTO;
import com.example.wellnesscoach.domain.chatGPT.dto.QuestionRequestDTO;
import com.example.wellnesscoach.domain.meal.AgingType;
import com.example.wellnesscoach.domain.meal.Meal;
import com.example.wellnesscoach.domain.chatGPT.service.ChatGPTService;
import com.example.wellnesscoach.domain.meal.repository.MealRepository;
import com.example.wellnesscoach.domain.recommendation.service.RecommendationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MealService {

    private final ChatGPTService chatGPTService;
    private final MealRepository mealRepository;
    private final RecommendationService recommendationService;

    public MealService(ChatGPTService chatGPTService, MealRepository mealRepository, RecommendationService recommendationService) {
        this.chatGPTService = chatGPTService;
        this.mealRepository = mealRepository;
        this.recommendationService = recommendationService;
    }

    public void analyzingMeal(Meal meal) {
        String meal1 = String.format("%s에 단순당, 정제곡물, 적색육, 탄수화물이 들어갔는지 JSON 형식으로만 존댓말로 답변해줘. 다른 말은 하지 말고, 다음과 같은 형식으로 대답해줘: { \"단순당\": true/false, \"정제곡물\": true/false, \"적색육\": true/false, \"탄수화물\": true/false }", meal.getMenuName());
        QuestionRequestDTO request1 = new QuestionRequestDTO(meal1);
        ChatGPTResponseDTO response1 = chatGPTService.askQuestion(request1);

        try {
            addMeal(meal, response1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChatGPTResponseDTO solutionMeal(String menuName, String type) {
        String solution = String.format(
                "%s는 가속노화 3단계인 가속/유의/저속 중 %s에 속하는 음식이야. 가속 혹은 유의 단계일 경우에는 가속노화를 줄이기 위한 대체 재료나 레시피 솔루션을 제공해주고 저속 단계일 경우에는 해당 음식이 저속노화에 도움이 되는 이유나 저속노화를 더욱 촉진시키기 위한 팁을 제공해줘. 다른 말은 하지 말고, 다음과 같은 형식으로 대답해줘: {\"솔루션\": \"가속, 유의일 경우 가속노화를 줄이기 위한 대체 재료나 레시피 솔루션, 저속일 경우 해당 음식이 저속노화에 도움이 되는 이유 혹은 저속노화를 더욱 촉진시키기 위한 팁\" }",
                menuName,
                type
        );
        QuestionRequestDTO request = new QuestionRequestDTO(solution);
        ChatGPTResponseDTO response = chatGPTService.askQuestion(request);

        return response;
    }

    public void analyzingDrink(Meal meal) {
        String menu = meal.getMenuName();
        String drink = String.format("%s에 첨가된 당을 통해 단계: 적정, 주의, 위험 중 하나를 골라주시고, 솔루션: 주의 또는 위험일 경우 가속노화 관련 솔루션을 3줄 이내로, 적정일 경우 해당 음식에 대한 저속노화 조언을 3줄 이내로 제공해 주세요. 다양한 예시를 포함해 주세요. 다른 말은 하지 말고, JSON 형식으로 다음과 같은 형식으로 존댓말로 답변해 주세요: { \"단계\": \"적정\"/\"주의\"/\"위험\", \"솔루션\": \"가속노화 솔루션 (주의 또는 위험일 경우), 해당 음식에 대한 저속노화 조언 (적정일 경우)\" }", menu);

        QuestionRequestDTO request = new QuestionRequestDTO(drink);
        ChatGPTResponseDTO response = chatGPTService.askQuestion(request);

        try {
            addDrink(meal, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMeal(Meal meal, ChatGPTResponseDTO response1) throws IOException {
        String analyzing = response1.getChoices().get(0).getMessage().getContent();
        analyzing = analyzing.replaceAll("```json", "").replaceAll("```", "").trim();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode1 = objectMapper.readTree(analyzing);

        boolean sugar = jsonNode1.get("단순당").asBoolean();
        boolean grain = jsonNode1.get("정제곡물").asBoolean();
        boolean redmeat = jsonNode1.get("적색육").asBoolean();
        boolean carbohydrate = jsonNode1.get("탄수화물").asBoolean();

        Integer score = calculateMeal(sugar, grain, redmeat, carbohydrate);

        String type;
        if (0 <= score && score < 4) type = "가속";
        else if (4 <= score && score < 7) type = "유의";
        else type = "저속";

        ChatGPTResponseDTO response2 = solutionMeal(meal.getMenuName(), type);

        String gptSolution = response2.getChoices().get(0).getMessage().getContent();
        gptSolution = gptSolution.replaceAll("```json", "").replaceAll("```", "").trim();

        JsonNode jsonNode2 = objectMapper.readTree(gptSolution);

        String solution = jsonNode2.get("솔루션").asText();

        meal.updateMeal(
                meal.getCheckup(),
                meal.getMenuType(),
                meal.getMenuName(),
                judgeAgingType(type),
                sugar,
                grain,
                redmeat,
                carbohydrate,
                score,
                solution
        );
        mealRepository.save(meal);

        // 여기서 추천 시스템 호출!!!
        if (!type.equals("저속")) {
            recommendationService.analyzeAlternativeFood(meal);
        }
    }

    public AgingType judgeAgingType(String type) {
        AgingType agingType;
        if (type == "저속") agingType = AgingType.PROPER;
        else if (type == "유의") agingType = AgingType.CAUTION;
        else agingType = AgingType.DANGER;
        return agingType;
    }

    public void addDrink(Meal meal, ChatGPTResponseDTO response) throws IOException {
        String content = response.getChoices().get(0).getMessage().getContent();
        content = content.replaceAll("```json", "").replaceAll("```", "").trim();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);

        String type = jsonNode.get("단계").asText();
        String solution = jsonNode.get("솔루션").asText();

        Integer score = 10;
        AgingType agingType = null;

        switch (type) {
            case "적정":
                agingType = AgingType.PROPER;
                score = 10;
                break;
            case "주의":
                agingType = AgingType.CAUTION;
                score = 6;
                break;
            case "위험":
                agingType = AgingType.DANGER;
                score = 2;
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 단계 유형입니다: " + type);
        }

        meal.updateMeal(
                meal.getCheckup(),
                meal.getMenuType(),
                meal.getMenuName(),
                agingType,
                false,
                false,
                false,
                false,
                score,
                solution
        );
        mealRepository.save(meal);

        if (!type.equals("적정")) {
            recommendationService.analyzeAlternativeFood(meal);
        }
    }

    public Integer calculateMeal(boolean sugar, boolean grain, boolean redmeat, boolean carbohydrate) {
        int score = 10;
        if (sugar) score -= 3;
        if (grain) score -= 3;
        if (redmeat) score -=  2;
        if (carbohydrate) score -= 2;

        return score;
    }
}
