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
                "%s는 가속노화를 촉진시키는 음식입니다. 이 음식의 모든 재료를 영양성분, 건강효과, 그리고 가속노화와의 관련성에 따라 상세히 분석해 주세요. 가속노화를 촉진시키는 주요 재료를 찾아내고, 해당 재료와 용도 및 맛이 비슷하면서 저속노화에 도움이 되는 재료로 대체해 주세요. 대체 이유도 함께 설명해 주세요. 다음과 같은 형식으로 대답해 주세요: {\"대체재료\": \"대체할 재료 이름\", \"대체대상재료\": \"대체될 재료 이름\", \"이유\": \"추천 이유\"}.",
                meal.getMenuName()
        );
        QuestionRequestDTO request = new QuestionRequestDTO(solution);
        ChatGPTResponseDTO response = chatGPTService.askQuestion(request);
        String answer = response.getChoices().get(0).getMessage().getContent();
        answer = answer.replaceAll("```json", "").replaceAll("```", "").trim();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(answer);

        String alternativeIngredient = jsonNode.get("대체재료").asText();
        String targetIngredient = jsonNode.get("대체대상재료").asText();

        String productLink = createRecommend(alternativeIngredient);

        Recommendation recommendation = new Recommendation();
        recommendation.createRecommendation(meal, targetIngredient, alternativeIngredient, productLink, meal.getCheckup().getUser());

        recommendationRepository.save(recommendation);
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
