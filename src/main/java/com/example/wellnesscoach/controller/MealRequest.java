package com.example.wellnesscoach.controller;

import com.example.wellnesscoach.domain.enums.MenuType;

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
