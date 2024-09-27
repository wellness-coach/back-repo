package com.example.wellnesscoach.service;

import com.example.wellnesscoach.domain.Checkup;
import com.example.wellnesscoach.domain.Meal;
import com.example.wellnesscoach.domain.enums.MenuType;
import com.example.wellnesscoach.domain.Result;
import com.example.wellnesscoach.repository.resultRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {
    final private resultRepository resultRepository;

    public ResultService(resultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @Transactional
    public void calculateScore(Checkup checkup) {

        List<Meal> meals = checkup.getMeals();
        int breakfastScore = 0;
        int lunchScore = 0;
        int dinnerScore = 0;
        int snackScore = 0;
        int drinkScore = 0;
        int count = 0;

        int breakfastCount = checkup.getMealNum(MenuType.BREAKFAST);
        int lunchCount = checkup.getMealNum(MenuType.LUNCH);
        int dinnerCount = checkup.getMealNum(MenuType.DINNER);
        int snackCount = checkup.getMealNum(MenuType.SNACK);
        int drinkCount = checkup.getMealNum(MenuType.DRINK);

        for (Meal meal : meals) {
            if (meal.getMenuType() == MenuType.BREAKFAST)
                breakfastScore += meal.getScore();
            else if (meal.getMenuType() == MenuType.LUNCH)
                lunchScore += meal.getScore();
            else if (meal.getMenuType() == MenuType.DINNER)
                dinnerScore += meal.getScore();
            else if (meal.getMenuType() == MenuType.SNACK)
                snackScore += meal.getScore();
            else
                drinkScore += meal.getScore();
        }

        int total = 0;

        if (breakfastCount != 0) {
            breakfastScore = breakfastScore / breakfastCount;
            Result result = new Result();
            result.saveResult(checkup, MenuType.BREAKFAST, breakfastScore);
            resultRepository.save(result);
            count++;
        }

        if (lunchCount != 0) {
            lunchScore = lunchScore / lunchCount;
            Result result = new Result();
            result.saveResult(checkup, MenuType.LUNCH, lunchScore);
            resultRepository.save(result);
            count++;
        }

        if (dinnerCount != 0) {
            dinnerScore = dinnerScore / dinnerCount;
            Result result = new Result();
            result.saveResult(checkup, MenuType.DINNER, dinnerScore);
            resultRepository.save(result);
            count++;
        }

        if (snackCount != 0) {
            snackScore = snackScore / snackCount;
            Result result = new Result();
            result.saveResult(checkup, MenuType.SNACK, snackScore);
            resultRepository.save(result);
            count++;
        }

        if (drinkCount != 0) {
            drinkScore = drinkScore / drinkCount;
            Result result = new Result();
            result.saveResult(checkup, MenuType.DRINK, drinkScore);
            resultRepository.save(result);
            count++;
        }

        total = (breakfastScore + lunchScore + dinnerScore + snackScore + drinkScore)/count;

        System.out.println(total);
        checkup.setScore(total);
    }
}
