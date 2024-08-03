package com.example.wellnesscoach.domain;

import com.example.wellnesscoach.domain.checkup.Checkup;
import com.example.wellnesscoach.domain.checkup.CheckupStatus;
import com.example.wellnesscoach.domain.checkup.repository.CheckupRepository;
import com.example.wellnesscoach.domain.checkup.service.CheckupService;
import com.example.wellnesscoach.domain.meal.AgingType;
import com.example.wellnesscoach.domain.recommendation.Recommendation;
import com.example.wellnesscoach.domain.recommendation.repository.RecommendationRepository;
import com.example.wellnesscoach.domain.recommendation.service.ProductResponse;
import com.example.wellnesscoach.domain.user.User;
import com.example.wellnesscoach.domain.user.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MainResponseController {
    private final UserRepository userRepository;
    private final CheckupService checkupService;
    private final CheckupRepository checkupRepository;
    private final RecommendationRepository recommendationRepository;

    public MainResponseController(UserRepository userRepository, CheckupService checkupService, CheckupRepository checkupRepository, RecommendationRepository recommendationRepository) {
        this.userRepository = userRepository;
        this.checkupService = checkupService;
        this.checkupRepository = checkupRepository;
        this.recommendationRepository = recommendationRepository;
    }

    @GetMapping("/mainPage")
    public MainResponse mainResponse(@RequestParam LocalDate date, @RequestParam Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        AgingType lastWeekAgingType = checkupService.lastWeekAgingType(user, date);
        CheckupStatus checkupStatus;
        Checkup checkup = checkupRepository.findByUserAndDate(user, date);

        if (checkup == null) checkupStatus = CheckupStatus.NOT_STARTED;
        else {
            checkupStatus = checkup.getCheckupStatus();
        }

        List<Recommendation> recommendations = recommendationRepository.findByUser(user);
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Recommendation recommendation : recommendations) {
            Boolean isScraped = recommendation.getScraps().stream().anyMatch(scrap -> scrap.getUser().equals(user));
            ProductResponse productResponse = ProductResponse.of(
                    recommendation.getRecommendId(),
                    recommendation.getTargetIngredient(),
                    recommendation.getProductName(),
                    recommendation.getProductLink(),
                    isScraped);
            productResponses.add(productResponse);
        }

        return MainResponse.of(user.getName(), lastWeekAgingType, checkupStatus, productResponses);
    }
}
