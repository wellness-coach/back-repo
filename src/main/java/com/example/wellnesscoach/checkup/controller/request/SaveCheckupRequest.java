package com.example.wellnesscoach.checkup.controller.request;

import com.example.wellnesscoach.meal.controller.request.MealRequest;

import java.time.LocalDate;
import java.util.List;

public record SaveCheckupRequest(
        Long checkupId,
        Long userId,
        LocalDate date,
        List<MealRequest> meals,
        String memo
) {

    public static SaveCheckupRequest of(
            final Long checkupId,
            final Long userId,
            final LocalDate date,
            final List<MealRequest> meals,
            final String memo
    ) {
        return new SaveCheckupRequest(checkupId, userId, date, meals, memo);
    }
}
