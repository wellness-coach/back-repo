package com.example.wellnesscoach.meal.controller.request;

import com.example.wellnesscoach.meal.MenuType;

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
