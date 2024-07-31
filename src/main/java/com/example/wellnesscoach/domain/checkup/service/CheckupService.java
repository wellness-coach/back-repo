package com.example.wellnesscoach.domain.checkup.service;

import com.example.wellnesscoach.domain.checkup.Checkup;
import com.example.wellnesscoach.domain.checkup.exception.NotFoundCheckupException;
import com.example.wellnesscoach.domain.checkup.repository.CheckupRepository;
import com.example.wellnesscoach.domain.checkup.service.request.SaveCheckupCommand;
import com.example.wellnesscoach.domain.checkup.service.response.CustomCheckupResponse;
import com.example.wellnesscoach.domain.meal.AgingType;
import com.example.wellnesscoach.domain.meal.MenuType;
import com.example.wellnesscoach.domain.meal.service.response.DrinkResultResponse;
import com.example.wellnesscoach.domain.meal.service.response.MealResponse;
import com.example.wellnesscoach.domain.meal.service.response.MealResultResponse;
import com.example.wellnesscoach.domain.recommendation.Recommendation;
import com.example.wellnesscoach.domain.result.Result;
import com.example.wellnesscoach.domain.result.repository.resultRepository;
import com.example.wellnesscoach.domain.result.service.response.ScoreResponse;
import com.example.wellnesscoach.domain.user.User;
import com.example.wellnesscoach.domain.meal.Meal;
import com.example.wellnesscoach.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CheckupService {

    private final com.example.wellnesscoach.domain.result.repository.resultRepository resultRepository;
    private CheckupRepository checkupRepository;
    private UserRepository userRepository;

    public CheckupService(CheckupRepository checkupRepository, UserRepository userRepository, resultRepository resultRepository) {
        this.checkupRepository = checkupRepository;
        this.userRepository = userRepository;
        this.resultRepository = resultRepository;
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
            checkup.submit(
                    user,
                    saveCheckupCommand.date(),
                    new ArrayList<>(), // 먼저 비어있는 리스트로 초기화
                    saveCheckupCommand.memo()
            );
            checkup = checkupRepository.save(checkup); //
        }

        final Checkup finalCheckup = checkup;

        List<Meal> meals = saveCheckupCommand.mealCommands().stream()
                .map(mealRequest -> {
                    Meal meal = new Meal();
                    meal.updateMeal(
                            finalCheckup,
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

    public CustomCheckupResponse getReport(User user, LocalDate date) {
        Map<String, List<Object>> meals = new HashMap<>();
        for (MenuType menuType : MenuType.values()) {
            meals.put(menuType.name(), new ArrayList<>());
        }

        Checkup checkup = checkupRepository.findByDate(date);
        List<Checkup> checkupList = checkupRepository.findAllCheckupsByUser(user);
        AgingType recentAging;
        if (checkupList.size() == 1) recentAging = null;
        else {
            Optional<Checkup> recentCheckup =
                    checkupList.stream().sorted(Comparator.comparing(Checkup::getDate).reversed()).findFirst();
            recentAging = recentCheckup.get().getTodayAgingType(); //가장 최근의 agingType
        }

        List<Meal> mealList = checkup.getMeals(); //오늘 하루 메뉴들

        setScore(meals, checkup);

        for (Meal meal : mealList) {
            if (meal.getMenuType() != MenuType.DRINK) setMeal(meals, checkup, meal);
            else setDrink(meals, checkup, meal);
        }

        return CustomCheckupResponse
                .from(user.getId(), date, checkup.getMemo(), recentAging, checkup.getTodayAgingType(), meals);
    }

    public void setMeal(Map<String, List<Object>> meals, Checkup checkup, Meal meal){
        String type = meal.getMenuType().name();
        Recommendation recommendation = meal.getRecommendation();

        MealResultResponse mealResult
                = MealResultResponse.builder()
                .menuName(meal.getMenuName())
                .sugar(meal.getSugar())
                .grain(meal.getGrain())
                .redmeat(meal.getRedmeat())
                .carbohydrate(meal.getCarbohydrate())
                .solution(meal.getSolution())
                .targetProductName(meal.getAgingType()!= AgingType.PROPER ? recommendation.getTargetIngredient() : null)
                .productName(meal.getAgingType()!= AgingType.PROPER ? recommendation.getProductName() : null)
                .productLink(meal.getAgingType()!= AgingType.PROPER ? recommendation.getProductLink() : null)
                .build();

        meals.get(type).add(mealResult);
    }

    public void setDrink(Map<String, List<Object>> meals, Checkup checkup, Meal meal){
        String type = meal.getMenuType().name();
        Recommendation recommendation = meal.getRecommendation();

        DrinkResultResponse drinkResult
                = DrinkResultResponse.builder()
                .menuName(meal.getMenuName())
                .sugar(meal.getSugar())
                .solution(meal.getSolution())
                .targetProductName(meal.getAgingType()== AgingType.PROPER ? recommendation.getTargetIngredient() : null)
                .productName(meal.getAgingType()== AgingType.PROPER ? recommendation.getProductName() : null)
                .productLink(meal.getAgingType()== AgingType.PROPER ? recommendation.getProductLink() : null)
                .build();

        meals.get(type).add(drinkResult);
    }

    public void setScore(Map<String, List<Object>> meals, Checkup checkup) {
        List<Result> resultList = resultRepository.findByCheckup(checkup);

        for (Result result : resultList) {
            ScoreResponse scoreResponse = ScoreResponse.builder()
                    .score(result.getScore())
                    .build();
            meals.get(result.getMenuType().name()).add(scoreResponse);
        }
    }
}