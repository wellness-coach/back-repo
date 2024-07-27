package com.example.wellnesscoach.checkup.controller;

import com.example.wellnesscoach.chatGPT.dto.ChatGPTResponseDTO;
import com.example.wellnesscoach.checkup.Checkup;
import com.example.wellnesscoach.checkup.MenuItem;
import com.example.wellnesscoach.checkup.MenuType;
import com.example.wellnesscoach.checkup.controller.request.SaveCheckupRequest;
import com.example.wellnesscoach.checkup.service.CheckupService;
import com.example.wellnesscoach.checkup.service.request.MenuItemCommand;
import com.example.wellnesscoach.checkup.service.request.SaveCheckupCommand;
import com.example.wellnesscoach.checkup.service.response.SaveCheckupResponse;
import com.example.wellnesscoach.meal.service.MealService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/checkup")
public class CheckupController {

    private final CheckupService checkupService;
    private final MealService mealService;

    public CheckupController(CheckupService checkupService, MealService mealService) {
        this.checkupService = checkupService;
        this.mealService = mealService;
    }

    @ResponseBody
    @PostMapping("/save")
    public ResponseEntity<SaveCheckupResponse> saveCheckup(@RequestBody SaveCheckupRequest saveCheckupRequest) {

        List<MenuItemCommand> menuItemCommands = saveCheckupRequest.menuItems().stream()
                .map(menuItemRequest -> MenuItemCommand.of(menuItemRequest.type(), menuItemRequest.name()))
                .collect(Collectors.toList());

        SaveCheckupCommand saveCheckupCommand = SaveCheckupCommand.of(
                saveCheckupRequest.checkupId(),
                saveCheckupRequest.userId(),
                saveCheckupRequest.date(),
                menuItemCommands,
                saveCheckupRequest.memo()
        );

        Checkup savedCheckup = checkupService.saveCheckup(saveCheckupCommand);
        SaveCheckupResponse response = SaveCheckupResponse.of(savedCheckup);
        return ResponseEntity.ok().body(response);
    }

    @ResponseBody
    @PostMapping("/submit")
    public void submitCheckup(@RequestBody SaveCheckupRequest saveCheckupRequest) {

        List<MenuItemCommand> menuItemCommands = saveCheckupRequest.menuItems().stream()
                .map(menuItemRequest -> MenuItemCommand.of(menuItemRequest.type(), menuItemRequest.name()))
                .collect(Collectors.toList());

        SaveCheckupCommand saveCheckupCommand = SaveCheckupCommand.of(
                saveCheckupRequest.checkupId(),
                saveCheckupRequest.userId(),
                saveCheckupRequest.date(),
                menuItemCommands,
                saveCheckupRequest.memo()
        );

        Checkup checkup = checkupService.submitCheckup(saveCheckupCommand);

        // meal Service 분석 시작
        List<String> menus = checkup.getMenuItems().stream()
                .map(MenuItem::getName)
                .collect(Collectors.toList());

        for (MenuItem menuItem : checkup.getMenuItems()) {
            if (menuItem.getType() == MenuType.DRINK) {
                mealService.analyzingDrink(menuItem);
            }
            else {
                mealService.analyzingMeal(menuItem);
            }
        }

        /*for (String menu : menus) {
            mealService.analyzingMeal(menu);
        }*/
    }
}