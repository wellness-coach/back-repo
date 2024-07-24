package com.example.wellnesscoach.checkup.controller.request;

import com.example.wellnesscoach.checkup.MenuType;

public record MenuItemRequest(
        MenuType type,
        String name
) {
    public static MenuItemRequest of(
            final MenuType type,
            final String name
    ) {
        return new MenuItemRequest(type, name);
    }
}
