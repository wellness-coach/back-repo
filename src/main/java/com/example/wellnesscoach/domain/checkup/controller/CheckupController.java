package com.example.wellnesscoach.domain.checkup.controller;

import com.example.wellnesscoach.domain.checkup.Checkup;
import com.example.wellnesscoach.domain.checkup.service.request.SaveCheckupCommand;
import com.example.wellnesscoach.domain.checkup.service.response.SaveCheckupResponse;
import com.example.wellnesscoach.domain.meal.MenuType;
import com.example.wellnesscoach.domain.checkup.controller.request.SaveCheckupRequest;
import com.example.wellnesscoach.domain.checkup.service.CheckupService;
import com.example.wellnesscoach.domain.meal.Meal;
import com.example.wellnesscoach.domain.meal.service.MealService;
import com.example.wellnesscoach.domain.meal.service.request.MealCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

        List<MealCommand> mealCommands = saveCheckupRequest.meals().stream()
                .map(mealRequest -> MealCommand.of(mealRequest.menuType(), mealRequest.menuName()))
                .collect(Collectors.toList());

        SaveCheckupCommand saveCheckupCommand = SaveCheckupCommand.of(
                saveCheckupRequest.checkupId(),
                saveCheckupRequest.userId(),
                saveCheckupRequest.date(),
                mealCommands,
                saveCheckupRequest.memo()
        );

        Checkup savedCheckup = checkupService.saveCheckup(saveCheckupCommand);
        SaveCheckupResponse response = SaveCheckupResponse.of(savedCheckup);
        return ResponseEntity.ok().body(response);
    }

    @ResponseBody
    @PostMapping("/submit")
    public void submitCheckup(@RequestBody SaveCheckupRequest saveCheckupRequest) {

        List<MealCommand> mealCommands = saveCheckupRequest.meals().stream()
                .map(mealRequest -> MealCommand.of(mealRequest.menuType(), mealRequest.menuName()))
                .collect(Collectors.toList());

        SaveCheckupCommand saveCheckupCommand = SaveCheckupCommand.of(
                saveCheckupRequest.checkupId(),
                saveCheckupRequest.userId(),
                saveCheckupRequest.date(),
                mealCommands,
                saveCheckupRequest.memo()
        );

        Checkup checkup = checkupService.submitCheckup(saveCheckupCommand);

        //식사 분석 및 점수 매기기
        for (Meal meal : checkup.getMeals()) {
            if (meal.getMenuType() == MenuType.DRINK) {
                mealService.analyzingDrink(meal);
            }
            else {
                mealService.analyzingMeal(meal);
            }
        }

        //checkupService.calculateScore(checkup);

    }
}