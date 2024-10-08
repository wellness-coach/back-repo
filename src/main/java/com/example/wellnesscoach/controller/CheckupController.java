package com.example.wellnesscoach.controller;

import com.example.wellnesscoach.domain.Checkup;
import com.example.wellnesscoach.repository.CheckupRepository;
import com.example.wellnesscoach.service.request.SaveCheckupCommand;
import com.example.wellnesscoach.service.response.CustomCheckupResponse;
import com.example.wellnesscoach.service.response.SaveCheckupResponse;
import com.example.wellnesscoach.domain.enums.MenuType;
import com.example.wellnesscoach.service.CheckupService;
import com.example.wellnesscoach.domain.Meal;
import com.example.wellnesscoach.service.MealService;
import com.example.wellnesscoach.service.request.MealCommand;
import com.example.wellnesscoach.service.ResultService;
import com.example.wellnesscoach.domain.User;
import com.example.wellnesscoach.repository.UserRepository;
import com.example.wellnesscoach.global.CustomException;
import com.example.wellnesscoach.global.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
        SaveCheckupCommand saveCheckupCommand = SaveCheckupCommand.of(
                null,
                saveCheckupRequest.userId(),
                saveCheckupRequest.date(),
                saveCheckupRequest.meals().stream()
                        .map(mealRequest -> MealCommand.of(mealRequest.menuType(), mealRequest.menuName()))
                        .collect(Collectors.toList()),
                saveCheckupRequest.memo()
        );

        Checkup savedCheckup = checkupService.saveCheckup(saveCheckupCommand);
        SaveCheckupResponse response = SaveCheckupResponse.of(savedCheckup);
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/submit")
    public String submitCheckup(@RequestBody SaveCheckupRequest saveCheckupRequest) {
        User user = userRepository.findByUserId(saveCheckupRequest.userId());
        Checkup existingCheckup = checkupRepository.findByUserAndDate(user,saveCheckupRequest.date());

        if (existingCheckup != null){
            checkupService.deleteCheckup(existingCheckup);
        }

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

        int num_meals = checkup.getMeals().size();

        //식사 분석 및 점수 매기기
        ExecutorService executorService = Executors.newFixedThreadPool(num_meals);
        List<Future<?>> futures = new ArrayList<>();

        for (Meal meal : checkup.getMeals()) {
            futures.add(executorService.submit(() -> {
                if (meal.getMenuType() == MenuType.DRINK) {
                    mealService.analyzingDrink(meal);
                }
                else {
                    mealService.analyzingMeal(meal);
                }
            }));
        }

        executorService.shutdown();

        // 모든 작업이 완료될 때까지 기다림
        for (Future<?> future : futures) {
            try {
                future.get(); // 작업이 완료될 때까지 기다림
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                // 예외 처리 필요시 여기서 수행
            }
        }

        resultService.calculateScore(checkup);
        return "success";
    }


    @GetMapping("/report")
    public CustomCheckupResponse reportCheckup(@RequestParam Long userId, @RequestParam LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Checkup checkup = checkupRepository.findByUserAndDate(user, date);
        if (checkup == null) {
            return null;
        }

        return checkupService.getReport(user, date);
    }

    @DeleteMapping("/delete/report")
    public ResponseEntity<?> reportDelete(@RequestParam Long userId, @RequestParam LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Checkup checkup = checkupRepository.findByUserAndDate(user, date);
        if (checkup == null) {
            return null;
        }

        checkupRepository.delete(checkup);

        return ResponseEntity.ok().body("delete success");
    }

    @GetMapping("/get")
    public ResponseEntity<SaveCheckupResponse> getCheckup(@RequestParam Long userId, @RequestParam LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Checkup checkup = checkupRepository.findByUserAndDate(user, date);
        if (checkup == null) {
            throw new CustomException(ErrorCode.CHECKUP_NOT_FOUND);
        }

        SaveCheckupResponse response = SaveCheckupResponse.of(checkup);
        return ResponseEntity.ok().body(response);
    }
}