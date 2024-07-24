package com.example.wellnesscoach.checkup.controller.request;

import java.time.LocalDate;
import java.util.List;

public record SaveCheckupRequest(
        Long checkupId,
        Long userId,
        LocalDate date,
        List<MenuItemRequest> menuItems,
        String memo
) {

    public static SaveCheckupRequest of(
            final Long checkupId,
            final Long userId,
            final LocalDate date,
            final List<MenuItemRequest> menuItems,
            final String memo
    ) {
        return new SaveCheckupRequest(checkupId, userId, date, menuItems, memo);
    }
}
