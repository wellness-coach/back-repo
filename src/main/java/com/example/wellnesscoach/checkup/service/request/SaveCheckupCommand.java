package com.example.wellnesscoach.checkup.service.request;

import java.time.LocalDate;
import java.util.List;

public record SaveCheckupCommand(
        Long checkupId,
        Long userId,
        LocalDate date,
        List<MenuItemCommand> menuItemCommands,
        String memo
) {

    public static SaveCheckupCommand of(
            final Long checkupId,
            final Long userId,
            final LocalDate date,
            final List<MenuItemCommand> menuItemCommands,
            final String memo
    ) {
        return new SaveCheckupCommand(checkupId, userId, date, menuItemCommands, memo);
    }
}
