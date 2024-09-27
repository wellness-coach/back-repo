package com.example.wellnesscoach.service.request;

import java.time.LocalDate;
import java.util.List;

public record SaveCheckupCommand(
        Long checkupId,
        Long userId,
        LocalDate date,
        List<MealCommand> mealCommands,
        String memo
) {

    public static SaveCheckupCommand of(
            final Long checkupId,
            final Long userId,
            final LocalDate date,
            final List<MealCommand> mealCommands,
            final String memo
    ) {
        return new SaveCheckupCommand(checkupId, userId, date, mealCommands, memo);
    }
}
