package com.example.wellnesscoach.meal.service;

import com.example.wellnesscoach.chatGPT.dto.ChatGPTResponseDTO;
import com.example.wellnesscoach.chatGPT.dto.QuestionRequestDTO;
import com.example.wellnesscoach.chatGPT.service.ChatGPTService;
import com.example.wellnesscoach.checkup.MenuItem;
import com.example.wellnesscoach.meal.AgingType;
import com.example.wellnesscoach.meal.Meal;
import com.example.wellnesscoach.meal.repository.MealRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MealService {

    private final ChatGPTService chatGPTService;
    private final MealRepository mealRepository;

    public MealService(ChatGPTService chatGPTService, MealRepository mealRepository) {
        this.chatGPTService = chatGPTService;
        this.mealRepository = mealRepository;
    }

    public void analyzingMeal(MenuItem menuItem) {
        String menu = menuItem.getName();
        String meal1 = String.format("%s에 단순당, 정제곡물, 적색육, 탄수화물이 들어갔는지 JSON 형식으로만 존댓말로 답변해줘. 다른 말은 하지 말고, 다음과 같은 형식으로 대답해줘: { \"단순당\": true/false, \"정제곡물\": true/false, \"적색육\": true/false, \"탄수화물\": true/false }", menu);
        String meal2 = String.format("%s가 가속노화를 불러일으키는지 저속노화를 불러일으키는지 JSON 형식으로만 존댓말로 답변해줘. 가속노화라면 솔루션과 대체 음식을 제공해줘. 다른 말은 하지 말고, 다음과 같은 형식으로 대답해줘: { \"노화유형\": \"가속노화\", \"솔루션\": \"가속노화를 줄이기 위한 대체 재료나 레시피 솔루션\", \"대체음식추천\": [{ \"이름\": \"대체음식 이름\", \"상품설명\": \"대체음식 설명\", \"추천이유\": \"대체음식을 추천하는 이유\" }] } 또는 { \"노화유형\": \"저속노화\", \"솔루션\": \"해당 음식이 저속노화에 도움이 되는 이유 혹은 저속노화를 더욱 촉진시키기 위한 팁\" }", menu);
        QuestionRequestDTO request1 = new QuestionRequestDTO(meal1);
        QuestionRequestDTO request2 = new QuestionRequestDTO(meal2);

        ChatGPTResponseDTO response1 = chatGPTService.askQuestion(request1);
        ChatGPTResponseDTO response2 = chatGPTService.askQuestion(request2);

        try {
            addMeal(menuItem, response1 ,response2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void analyzingDrink(MenuItem menuItem) {
        String menu = menuItem.getName();
        String drink = String.format("%s에 첨가된 당을 통해 단계: 적정, 주의, 위험 중 하나를 골라주고, 솔루션: 주의 또는 위험일 경우 3줄 이내 가속노화 관련 솔루션을 제공하고, 적정일 경우 저속노화 관련 조언을 해줘. 다른 말은 하지 말고, JSON 형식으로 다음과 같은 형식으로 존댓말로 답변해줘: { \"단계\": \"적정\"/\"주의\"/\"위험\", \"솔루션\": \"가속노화 솔루션 (주의 또는 위험일 경우), 해당 음식에 대한 저속노화 조언 (적정일 경우)\" }", menu);

        QuestionRequestDTO request = new QuestionRequestDTO(drink);
        ChatGPTResponseDTO response = chatGPTService.askQuestion(request);

        try {
            addDrink(menuItem, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMeal(MenuItem menuItem, ChatGPTResponseDTO response1, ChatGPTResponseDTO response2) throws IOException {
        String content1 = response1.getChoices().get(0).getMessage().getContent();
        String content2 = response2.getChoices().get(0).getMessage().getContent();
        content1 = content1.replaceAll("```json", "").replaceAll("```", "").trim();
        content2 = content2.replaceAll("```json", "").replaceAll("```", "").trim();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode1 = objectMapper.readTree(content1);
        JsonNode jsonNode2 = objectMapper.readTree(content2);

        boolean sugar = jsonNode1.get("단순당").asBoolean();
        boolean grain = jsonNode1.get("정제곡물").asBoolean();
        boolean redmeat = jsonNode1.get("적색육").asBoolean();
        boolean carbohydrate = jsonNode1.get("탄수화물").asBoolean();
        String type = jsonNode2.get("노화유형").asText();

        AgingType agingType = null;
        if (type.equals("가속노화")) agingType = AgingType.ACCELERATED_AGING;
        else if (type.equals("저속노화")) agingType = AgingType.SLOW_AGING;

        String solution = jsonNode2.get("솔루션").asText();

        Integer score = calculateMeal(sugar, grain, redmeat, carbohydrate);

        Meal meal = new Meal();
        meal.updateMeal(
                menuItem,
                menuItem.getName(),
                agingType,
                sugar,
                grain,
                redmeat,
                carbohydrate,
                score,
                solution
        );

        mealRepository.save(meal);
    }

    public void addDrink(MenuItem menuItem, ChatGPTResponseDTO response) throws IOException {
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

        Meal meal = new Meal();
        meal.updateMeal(
                menuItem,
                menuItem.getName(),
                agingType,
                false,
                false,
                false,
                false,
                score,
                solution
        );
        mealRepository.save(meal);
    }

    public int calculateMeal(boolean sugar, boolean grain, boolean redmeat, boolean carbohydrate) {
        int score = 10;
        if (sugar) score -= 3;
        if (grain) score -= 3;
        if (redmeat) score -=  2;
        if (carbohydrate) score -= 2;

        return score;
    }
}
