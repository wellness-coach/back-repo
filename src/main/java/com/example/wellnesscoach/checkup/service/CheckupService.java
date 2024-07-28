package com.example.wellnesscoach.checkup.service;

import com.example.wellnesscoach.checkup.Checkup;
import com.example.wellnesscoach.checkup.exception.NotFoundCheckupException;
import com.example.wellnesscoach.checkup.repository.CheckupRepository;
import com.example.wellnesscoach.checkup.service.request.SaveCheckupCommand;
import com.example.wellnesscoach.entity.User;
import com.example.wellnesscoach.meal.Meal;
import com.example.wellnesscoach.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckupService {

    private CheckupRepository checkupRepository;
    private UserRepository userRepository;

    public CheckupService(CheckupRepository checkupRepository, UserRepository userRepository) {
        this.checkupRepository = checkupRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Checkup saveCheckup(SaveCheckupCommand saveCheckupCommand) {
        Checkup checkup;

        User user = userRepository.findById(saveCheckupCommand.userId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (saveCheckupCommand.checkupId() != null) {
            checkup = checkupRepository.findById(saveCheckupCommand.checkupId())
                    .orElseThrow(() -> new NotFoundCheckupException("해당 진단지를 찾을 수 없습니다."));
        } else {
            checkup = new Checkup();
        }

        List<Meal> meals = saveCheckupCommand.mealCommands().stream()
                .map(mealRequest ->  {
                    Meal meal = new Meal();
                    meal.updateMeal(
                            checkup,
                            mealRequest.menuType(),
                            mealRequest.menuName(),
                            null,
                            false,
                            false,
                            false,
                            false,
                            0,
                            null
                    );
                    return meal;
                })
                .collect(Collectors.toList());

        checkup.update(
                user,
                saveCheckupCommand.date(),
                meals,
                saveCheckupCommand.memo()
        );
        return checkupRepository.save(checkup);
    }

    @Transactional
    public Checkup submitCheckup(SaveCheckupCommand saveCheckupCommand) {
        Checkup checkup;

        User user = userRepository.findById(saveCheckupCommand.userId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (saveCheckupCommand.checkupId() != null) {
            checkup = checkupRepository.findById(saveCheckupCommand.checkupId())
                    .orElseThrow(() -> new NotFoundCheckupException("해당 진단지를 찾을 수 없습니다."));
        } else {
            checkup = new Checkup();
        }

        List<Meal> meals = saveCheckupCommand.mealCommands().stream()
                .map(mealRequest -> {
                    Meal meal = new Meal();
                    meal.updateMeal(
                            checkup,
                            mealRequest.menuType(),
                            mealRequest.menuName(),
                            null,
                            false,
                            false,
                            false,
                            false,
                            0,
                            null);
                    return meal;
        })
                .collect(Collectors.toList());

        checkup.submit(
                user,
                saveCheckupCommand.date(),
                meals,
                saveCheckupCommand.memo()
        );
        return checkupRepository.save(checkup);
    }


    /*
    public Integer calculateScore(Checkup checkup) {
        int breakfastScore = 0;
        int lunchScore = 0;
        int dinnerScore = 0;
        int snackScore = 0;
        int drinkScore = 0;
        for (MenuItem menuItem  : checkup) {
            if (menuItem.getType() == MenuType.BREAKFAST)
                breakfastScore += menuItem.getMeal().getScore();
            else if (menuItem.getType() == MenuType.LUNCH)
                lunchScore += menuItem.getMeal().getScore();
            else if (menuItem.getType() == MenuType.DINNER)
                dinnerScore += menuItem.getMeal().getScore();
            else if (menuItem.getType() == MenuType.SNACK)
                snackScore += menuItem.getMeal().getScore();
            else
                drinkScore += menuItem.getMeal().getScore();
        };
        int total = 0;
        breakfastScore = breakfastScore/checkup.getMealNum(checkup, MenuType.BREAKFAST);
        lunchScore = lunchScore/checkup.getMealNum(checkup, MenuType.LUNCH);
        dinnerScore = dinnerScore/checkup.getMealNum(checkup, MenuType.DINNER);
        snackScore = snackScore/checkup.getMealNum(checkup, MenuType.SNACK);
        drinkScore = drinkScore/checkup.getMealNum(checkup, MenuType.DRINK);

        total = (breakfastScore + lunchScore + dinnerScore + snackScore + drinkScore)/5;
        return total;
    }*/
}
