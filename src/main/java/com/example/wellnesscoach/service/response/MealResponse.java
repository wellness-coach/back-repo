package com.example.wellnesscoach.service.response;

import com.example.wellnesscoach.domain.enums.MenuType;

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
