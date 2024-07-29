package com.example.wellnesscoach.domain.meal.service.response;

import com.example.wellnesscoach.domain.meal.MenuType;

public record MealResponse(
        MenuType menuType,
        String menuName
) {
    public static MealResponse of(
            final MenuType menuType,
            final String menuName
    ) {
        return new MealResponse(menuType, menuName);
    }
}
