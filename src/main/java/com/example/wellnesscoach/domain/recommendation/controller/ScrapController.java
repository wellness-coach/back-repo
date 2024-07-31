package com.example.wellnesscoach.domain.recommendation.controller;

import com.example.wellnesscoach.domain.recommendation.Recommendation;
import com.example.wellnesscoach.domain.recommendation.repository.RecommendationRepository;
import com.example.wellnesscoach.domain.recommendation.service.ScrapService;
import com.example.wellnesscoach.domain.user.User;
import com.example.wellnesscoach.domain.user.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scrap")
public class ScrapController {

    private final UserRepository userRepository;
    private final ScrapService scrapService;
    private final RecommendationRepository recommendationRepository;

    public ScrapController(UserRepository userRepository, ScrapService scrapService, RecommendationRepository recommendationRepository) {
        this.userRepository = userRepository;
        this.scrapService = scrapService;
        this.recommendationRepository = recommendationRepository;
    }

    @PostMapping("/add")
    public String addScrap(@RequestParam Long userId, @RequestParam Long recommendationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                        .orElseThrow(() -> new IllegalArgumentException("Recommendation not found"));

        scrapService.addScrap(user, recommendation);
        return "success";
    }

    @DeleteMapping("/cancel")
    public String cancelScrap(@RequestParam Long userId, @RequestParam Long recommendationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new IllegalArgumentException("Recommendation not found"));

        scrapService.cancelScrap(user, recommendation);
        return "success";
    }
}