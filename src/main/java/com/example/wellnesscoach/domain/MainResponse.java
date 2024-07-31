package com.example.wellnesscoach.domain;

import com.example.wellnesscoach.domain.checkup.CheckupStatus;
import com.example.wellnesscoach.domain.meal.AgingType;
import com.example.wellnesscoach.domain.recommendation.service.ProductResponse;

import java.util.List;

public record MainResponse(
        AgingType lastWeekAgingType,
        CheckupStatus checkupStatus,
        List<ProductResponse> products
) {
    public static MainResponse of(AgingType lastWeekAgingType, CheckupStatus checkupStatus, List<ProductResponse> products) {
        return new MainResponse(lastWeekAgingType, checkupStatus, products);
    }
}