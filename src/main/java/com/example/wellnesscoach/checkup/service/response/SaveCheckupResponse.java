package com.example.wellnesscoach.checkup.service.response;

import com.example.wellnesscoach.checkup.Checkup;
import com.example.wellnesscoach.checkup.CheckupStatus;
import com.example.wellnesscoach.meal.service.response.MealResponse;

import java.time.LocalDate;
import java.util.List;

public record SaveCheckupResponse(
        Long checkupId,
        Long userId,
        LocalDate date,
        List<MealResponse> mealResponses,
        String memo,
        CheckupStatus checkupStatus
) {
    public static SaveCheckupResponse of(Checkup checkup){
        List<MealResponse> meals = checkup.getMeals().stream()
                .map(meal -> new MealResponse(meal.getMenuType(), meal.getMenuName()))
                .toList();

        return new SaveCheckupResponse(
                checkup.getCheckupId(),
                checkup.getUser().getId(),
                checkup.getDate(),
                meals,
                checkup.getMemo(),
                checkup.getCheckupStatus()
        );
    }
}
