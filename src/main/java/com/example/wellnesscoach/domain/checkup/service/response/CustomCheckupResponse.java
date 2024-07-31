package com.example.wellnesscoach.domain.checkup.service.response;

import com.example.wellnesscoach.domain.meal.AgingType;
import com.example.wellnesscoach.domain.meal.service.response.MealResultResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record CustomCheckupResponse(
        Long userId,
        LocalDate date,
        String memo,
        AgingType recentAgingType,
        AgingType todayAgingType,
        Map<String, List<Object>> meals
) {
    public static CustomCheckupResponse from(Long userId, LocalDate date, String memo, AgingType recentAgingType, AgingType todayAgingType, Map<String, List<Object>> meals) {
        return new CustomCheckupResponse(
                userId, date, memo, recentAgingType, todayAgingType, meals
        );
    }
}