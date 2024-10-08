package com.example.wellnesscoach.service;

import com.example.wellnesscoach.dto.ChatGPTResponseDTO;
import com.example.wellnesscoach.dto.QuestionRequestDTO;
import com.example.wellnesscoach.domain.Meal;
import com.example.wellnesscoach.domain.ApiExamSearchBlog;
import com.example.wellnesscoach.domain.Recommendation;
import com.example.wellnesscoach.repository.RecommendationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
public class RecommendationService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    private final ApiExamSearchBlog apiExamSearchBlog;

    private final RecommendationRepository recommendationRepository;
    private final ChatGPTService chatGPTService;

    public RecommendationService(RecommendationRepository recommendationRepository, ChatGPTService chatGPTService, ApiExamSearchBlog apiExamSearchBlog) {
        this.recommendationRepository = recommendationRepository;
        this.chatGPTService = chatGPTService;
        this.apiExamSearchBlog = apiExamSearchBlog;
    }

    public void analyzeAlternativeFood(Meal meal) throws IOException {
        String solution = String.format(
                "%s는 가속노화를 촉진시키는 음식 또는 음료야. 음식일 경우 %s에 들어가는 음식 재료들을 분석해보고 주재료 중에서 가속노화를 촉진시키는걸 찾아서 같은 카테고리의 다른 저속노화에 좋은 재료로 대체해주고 그 이유를 적어줘. 대체 재료는 노화에 좋은 재료로 선택해줘. 예시를 보여주자면 마라탕일 경우 밀가루 면을 두부 면으로 대체하면 저속노화에 좋겠지?  음료일 경우에는 대체 음료로 특정한 무가당 음료를 추천해줘. 그리고 실제로 해당 재료를 대체할 수 있을만한 걸로 해줘. 식감이나 그 재료나 대체 음료가 하는 역할이 비슷하도록. 다른 말은 하지 말고, 다음과 같은 형식으로 대답해줘: {\"대체재료\": \"대체할 재료 이름\", \"대체대상재료\": \"대체될 재료 이름\", \"이유\": \"대체대상재료로 대체해야하는 이유\"}",
                meal.getMenuName(), meal.getMenuName()
        );
        QuestionRequestDTO request = new QuestionRequestDTO(solution);
        ChatGPTResponseDTO response = chatGPTService.askQuestion(request);
        String answer = response.getChoices().get(0).getMessage().getContent();

        // 응답 확인을 위한 로그 출력
        System.out.println("ChatGPT 응답: " + answer);

        // JSON 형식 검증 및 파싱
        answer = answer.replaceAll("```json", "").replaceAll("```", "").trim();

        // 예외 처리를 추가하여 문제가 되는 데이터를 식별합니다.
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(answer);

            String alternativeIngredient = jsonNode.get("대체재료").asText();
            String targetIngredient = jsonNode.get("대체대상재료").asText();

            String productLink = createRecommend(alternativeIngredient);

            Recommendation recommendation = new Recommendation();
            recommendation.createRecommendation(meal, targetIngredient, alternativeIngredient, productLink, meal.getCheckup().getUser());

            recommendationRepository.save(recommendation);
        } catch (Exception e) {
            System.err.println("JSON 파싱 오류: " + e.getMessage());
            throw new IOException("ChatGPT 응답을 파싱하는 도중 오류가 발생했습니다.", e);
        }
    }

    private String createRecommend(String productName) throws IOException{
        try {
            productName = URLEncoder.encode(productName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/shop?query=" + productName;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = apiExamSearchBlog.get(apiURL, requestHeaders);

        //System.out.println(responseBody);
        return customRecomment(responseBody);
    }

    private String customRecomment(String responseBody) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        JsonNode itemNode = jsonNode.get("item");
        String link = itemNode.get("link").asText();

        return link;
    }
}
