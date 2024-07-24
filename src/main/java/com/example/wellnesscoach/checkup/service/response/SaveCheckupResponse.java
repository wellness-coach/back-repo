package com.example.wellnesscoach.checkup.service.response;

import com.example.wellnesscoach.checkup.Checkup;
import com.example.wellnesscoach.checkup.CheckupStatus;
import com.example.wellnesscoach.checkup.controller.request.MenuItemRequest;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public record SaveCheckupResponse(
        Long checkupId,
        Long userId,
        LocalDate date,
        List<MenuItemResponse> menuItemRequests,
        String memo,
        CheckupStatus checkupStatus
) {
    public static SaveCheckupResponse of(Checkup checkup){
        List<MenuItemResponse> menuItems = checkup.getMenuItems().stream()
                .map(menuItem -> new MenuItemResponse(menuItem.getType(), menuItem.getName()))
                .toList();

        return new SaveCheckupResponse(
                checkup.getCheckupId(),
                checkup.getUser().getId(),
                checkup.getDate(),
                menuItems,
                checkup.getMemo(),
                checkup.getCheckupStatus()
        );
    }
}
