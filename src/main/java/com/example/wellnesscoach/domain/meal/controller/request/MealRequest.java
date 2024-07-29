package com.example.wellnesscoach.domain.meal.controller.request;

import com.example.wellnesscoach.domain.meal.MenuType;

public record MealRequest(
        MenuType menuType,
        String menuName
) {
    public static MealRequest of(
            final MenuType menuType,
            final String menuName
    ) {
        return new MealRequest(menuType, menuName);
    }
}
