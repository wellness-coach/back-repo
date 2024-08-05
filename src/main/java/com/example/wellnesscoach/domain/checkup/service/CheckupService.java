package com.example.wellnesscoach.domain.checkup.service;

import com.example.wellnesscoach.domain.checkup.Checkup;
import com.example.wellnesscoach.domain.checkup.CheckupStatus;
import com.example.wellnesscoach.domain.checkup.exception.NotFoundCheckupException;
import com.example.wellnesscoach.domain.checkup.repository.CheckupRepository;
import com.example.wellnesscoach.domain.checkup.service.request.SaveCheckupCommand;
import com.example.wellnesscoach.domain.checkup.service.response.CustomCheckupResponse;
import com.example.wellnesscoach.domain.checkup.service.response.SaveCheckupResponse;
import com.example.wellnesscoach.domain.meal.AgingType;
import com.example.wellnesscoach.domain.meal.MenuType;
import com.example.wellnesscoach.domain.meal.service.response.DrinkResultResponse;
import com.example.wellnesscoach.domain.meal.service.response.MealResultResponse;
import com.example.wellnesscoach.domain.recommendation.Recommendation;
import com.example.wellnesscoach.domain.recommendation.repository.ScrapRepoisitory;
import com.example.wellnesscoach.domain.recommendation.service.ProductResponse;
import com.example.wellnesscoach.domain.result.Result;
import com.example.wellnesscoach.domain.result.repository.resultRepository;
import com.example.wellnesscoach.domain.result.service.response.ScoreResponse;
import com.example.wellnesscoach.domain.user.User;
import com.example.wellnesscoach.domain.meal.Meal;
import com.example.wellnesscoach.domain.user.repository.UserRepository;
import com.example.wellnesscoach.global.CustomException;
import com.example.wellnesscoach.global.ErrorCode;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CheckupService {

    private final com.example.wellnesscoach.domain.result.repository.resultRepository resultRepository;
    private final ScrapRepoisitory scrapRepoisitory;
    private CheckupRepository checkupRepository;
    private UserRepository userRepository;

    public CheckupService(CheckupRepository checkupRepository, UserRepository userRepository, resultRepository resultRepository, ScrapRepoisitory scrapRepoisitory) {
        this.checkupRepository = checkupRepository;
        this.userRepository = userRepository;
        this.resultRepository = resultRepository;
        this.scrapRepoisitory = scrapRepoisitory;
    }

    @Transactional
    public Checkup saveCheckup(SaveCheckupCommand saveCheckupCommand) {
        User user = userRepository.findById(saveCheckupCommand.userId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Checkup checkup = checkupRepository.findByUserAndDate(user, saveCheckupCommand.date());

        if (checkup == null) {
            checkup = new Checkup();
        }

        Checkup finalCheckup = checkup;
        List<Meal> meals = saveCheckupCommand.mealCommands().stream()
                .map(mealRequest ->  {
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
    public void deleteCheckup(Checkup checkup) {
        checkupRepository.delete(checkup);
    }

    @Transactional
    public Checkup submitCheckup(SaveCheckupCommand saveCheckupCommand) {
        Checkup checkup;

        User user = userRepository.findById(saveCheckupCommand.userId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        checkup = new Checkup();
        checkup.submit(
                user,
                saveCheckupCommand.date(),
                new ArrayList<>(), // 먼저 비어있는 리스트로 초기화
                saveCheckupCommand.memo()
        );
        checkup = checkupRepository.save(checkup);

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

        Checkup checkup = checkupRepository.findByUserAndDate(user, date);

        if (checkup.getCheckupStatus() == CheckupStatus.IN_PROGRESS) {
            return null;
        }

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
            if (meal.getMenuType() != MenuType.DRINK) setMeal(meals, checkup, meal, user);
            else setDrink(meals, checkup, meal, user);
        }

        return CustomCheckupResponse
                .from(user.getUserId(), date, checkup.getMemo(), recentAging, checkup.getTodayAgingType(), meals);
    }

    public void setMeal(Map<String, List<Object>> meals, Checkup checkup, Meal meal, User user){
        String type = meal.getMenuType().name();
        Recommendation recommendation = meal.getRecommendation();
        Boolean isScraped = false;
        if (recommendation != null) {
            if (recommendation.getScraps() != null) {
            isScraped = recommendation.getScraps().stream().anyMatch(scrap -> scrap.getUser().equals(user));
            }
        }
        ProductResponse productResponse = null;
        if (meal.getAgingType() != AgingType.PROPER)
            productResponse = ProductResponse.of(recommendation.getRecommendId(), recommendation.getTargetIngredient(), recommendation.getProductName(), recommendation.getProductLink(), isScraped);

        MealResultResponse mealResult
                = MealResultResponse.builder()
                .menuName(meal.getMenuName())
                .sugar(meal.getSugar())
                .grain(meal.getGrain())
                .redmeat(meal.getRedmeat())
                .salt(meal.getSalt())
                .solution(meal.getSolution())
                .productResponse(productResponse)
                .build();

        meals.get(type).add(mealResult);
    }

    public void setDrink(Map<String, List<Object>> meals, Checkup checkup, Meal meal, User user){
        String type = meal.getMenuType().name();
        Recommendation recommendation = meal.getRecommendation();
        Boolean isScraped = false;
        if (recommendation != null) {
            if (recommendation.getScraps() != null) {
                isScraped = recommendation.getScraps().stream().anyMatch(scrap -> scrap.getUser().equals(user));
            }
        }

        ProductResponse productResponse = null;
        if (meal.getAgingType() != AgingType.PROPER) {
            productResponse = ProductResponse.of(recommendation.getRecommendId(), recommendation.getTargetIngredient(), recommendation.getProductName(), recommendation.getProductLink(), isScraped);
        }


        DrinkResultResponse drinkResult
                = DrinkResultResponse.builder()
                .menuName(meal.getMenuName())
                .sugar(meal.getSugar())
                .solution(meal.getSolution())
                .productResponse(productResponse)
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

    public AgingType lastWeekAgingType(User user, LocalDate date) {
        var oneWeekBefore = date.minusWeeks(1);
        var startOfWeek = oneWeekBefore.with(DayOfWeek.MONDAY);
        var endOfWeek = oneWeekBefore.with(DayOfWeek.SUNDAY);

        List<Checkup> checkupList = checkupRepository.findCheckupsByUserAndDateRange(user, startOfWeek, endOfWeek);
        List<AgingType> agingTypeList = checkupList.stream().map(Checkup::getTodayAgingType).toList();

        AgingType lastWeekAgingType = null;

        if (agingTypeList.isEmpty()) return lastWeekAgingType; //첫 유저인 경우 처리

        int total = 0;
        for (AgingType agingType : agingTypeList){
            total += agingType.ordinal();
        }
        int size = agingTypeList.size();
        float score = total/size;

        if (score < 0.5) lastWeekAgingType = AgingType.PROPER;
        else if (score < 1.5) lastWeekAgingType = AgingType.CAUTION;
        else lastWeekAgingType = AgingType.DANGER;

        return lastWeekAgingType;
    }

    public SaveCheckupResponse getCheckup(User user, LocalDate date) {
        Checkup checkup = checkupRepository.findByUserAndDate(user, date);
        if (checkup == null) {
            throw new CustomException(ErrorCode.CHECKUP_NOT_FOUND);
        }
        return SaveCheckupResponse.of(checkup);
    }
}