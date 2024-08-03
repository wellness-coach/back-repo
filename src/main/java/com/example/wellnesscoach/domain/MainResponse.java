package com.example.wellnesscoach.domain;

import com.example.wellnesscoach.domain.checkup.CheckupStatus;
import com.example.wellnesscoach.domain.meal.AgingType;
import com.example.wellnesscoach.domain.recommendation.service.ProductResponse;
import com.example.wellnesscoach.domain.user.User;

import java.util.List;

public record MainResponse(
        String name,
        AgingType lastWeekAgingType,
        CheckupStatus checkupStatus,
        List<ProductResponse> products
) {
    public static MainResponse of(String name, AgingType lastWeekAgingType, CheckupStatus checkupStatus, List<ProductResponse> products) {
        return new MainResponse(name, lastWeekAgingType, checkupStatus, products);
    }
}