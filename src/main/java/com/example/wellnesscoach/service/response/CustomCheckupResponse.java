package com.example.wellnesscoach.service.response;

import com.example.wellnesscoach.domain.enums.AgingType;

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