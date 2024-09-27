package com.example.wellnesscoach.domain;

import com.example.wellnesscoach.domain.enums.AgingType;
import com.example.wellnesscoach.service.response.ProductResponse;

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