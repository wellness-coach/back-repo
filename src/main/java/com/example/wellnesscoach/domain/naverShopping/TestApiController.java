package com.example.wellnesscoach.domain.naverShopping;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestApiController {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    private final ApiExamSearchBlog apiExamSearchBlog;

    public TestApiController(ApiExamSearchBlog apiExamSearchBlog) {
        this.apiExamSearchBlog = apiExamSearchBlog;
    }

    @GetMapping("/api_test")
    public void postApiTest(@RequestParam("query") String query) {
        String text = query;
        try {
            text = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/shop?query=" + text;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = apiExamSearchBlog.get(apiURL, requestHeaders);
        System.out.println(responseBody);
    }
}