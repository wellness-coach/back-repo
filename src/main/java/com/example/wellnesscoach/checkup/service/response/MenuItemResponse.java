package com.example.wellnesscoach.checkup.service.response;

import com.example.wellnesscoach.checkup.MenuType;

public record MenuItemResponse(
        MenuType type,
        String name
) {
    public static MenuItemResponse of(
            final MenuType type,
            final String name
    ) {
        return new MenuItemResponse(type, name);
    }
}
