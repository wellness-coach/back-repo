package com.example.wellnesscoach.domain.recommendation.service;

import com.example.wellnesscoach.domain.chatGPT.dto.ChatGPTResponseDTO;
import com.example.wellnesscoach.domain.chatGPT.dto.QuestionRequestDTO;
import com.example.wellnesscoach.domain.chatGPT.service.ChatGPTService;
import com.example.wellnesscoach.domain.meal.Meal;
import com.example.wellnesscoach.domain.naverShopping.ApiExamSearchBlog;
import com.example.wellnesscoach.domain.recommendation.Recommendation;
import com.example.wellnesscoach.domain.recommendation.repository.RecommendationRepository;
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
                "%s는 가속노화를 촉진시키는음식이야. 이 음식 재료들을 분석해보고 주재료 중에서 가속노화를 촉진시키는걸 찾아서 같은 카테고리의 다른 재료로 대체해주고 그 이유를 적어줘.. 대체 재료는 노화에 좋은 재료로 선택해줘. 다른 말은 하지 말고, 다음과 같은 형식으로 대답해줘: {\"대체재료\": \"대체할 재료 이름\", \"대체대상재료\": \"대체될 재료 이름\", \"이유\": \"추천 이유\"}",
                meal.getMenuName()
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
