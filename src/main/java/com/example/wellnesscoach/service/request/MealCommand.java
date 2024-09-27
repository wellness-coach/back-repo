package com.example.wellnesscoach.service.request;

import com.example.wellnesscoach.domain.enums.MenuType;

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
