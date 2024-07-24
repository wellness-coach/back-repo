package com.example.wellnesscoach.checkup.service.request;

import com.example.wellnesscoach.checkup.MenuType;

public record MenuItemCommand(
        MenuType type,
        String name
) {
    public static MenuItemCommand of(
            final MenuType type,
            final String name
    ) {
        return new MenuItemCommand(type, name);
    }
}
