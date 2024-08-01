package com.example.wellnesscoach.domain.checkup.controller;

import com.example.wellnesscoach.domain.checkup.Checkup;
import com.example.wellnesscoach.domain.checkup.repository.CheckupRepository;
import com.example.wellnesscoach.domain.checkup.service.request.SaveCheckupCommand;
import com.example.wellnesscoach.domain.checkup.service.response.CustomCheckupResponse;
import com.example.wellnesscoach.domain.checkup.service.response.SaveCheckupResponse;
import com.example.wellnesscoach.domain.meal.MenuType;
import com.example.wellnesscoach.domain.checkup.controller.request.SaveCheckupRequest;
import com.example.wellnesscoach.domain.checkup.service.CheckupService;
import com.example.wellnesscoach.domain.meal.Meal;
import com.example.wellnesscoach.domain.meal.service.MealService;
import com.example.wellnesscoach.domain.meal.service.request.MealCommand;
import com.example.wellnesscoach.domain.result.service.ResultService;
import com.example.wellnesscoach.domain.user.User;
import com.example.wellnesscoach.domain.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/checkup")
public class CheckupController {

    private final CheckupService checkupService;
    private final MealService mealService;
    private final ResultService resultService;
    private final UserRepository userRepository;
    private final CheckupRepository checkupRepository;

    public CheckupController(CheckupService checkupService, MealService mealService, ResultService resultService, UserRepository userRepository, CheckupRepository checkupRepository) {
        this.checkupService = checkupService;
        this.mealService = mealService;
        this.resultService = resultService;
        this.userRepository = userRepository;
        this.checkupRepository = checkupRepository;
    }


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


    @PostMapping("/submit")
    public String submitCheckup(@RequestBody SaveCheckupRequest saveCheckupRequest) {

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

        resultService.calculateScore(checkup);
        return "success";
    }


    @GetMapping("/report")
    public CustomCheckupResponse reportCheckup(@RequestParam Long userId, @RequestParam LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return checkupService.getReport(user, date);
    }
}