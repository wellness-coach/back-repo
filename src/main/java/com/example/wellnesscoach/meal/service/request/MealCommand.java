package com.example.wellnesscoach.meal.service.request;

import com.example.wellnesscoach.meal.MenuType;

public record MealCommand(
        MenuType menuType,
        String menuName
) {
    public static MealCommand of(
            final MenuType menuType,
            final String menuName
    ) {
        return new MealCommand(menuType, menuName);
    }
}
