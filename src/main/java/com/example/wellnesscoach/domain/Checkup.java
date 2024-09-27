package com.example.wellnesscoach.domain;

import com.example.wellnesscoach.domain.enums.AgingType;
import com.example.wellnesscoach.domain.enums.MenuType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Checkup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    private LocalDate date;

    @OneToMany(mappedBy = "checkup", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Meal> meals;

    private String memo;

    @Enumerated(EnumType.STRING)
    private CheckupStatus checkupStatus;

    @Enumerated(EnumType.STRING)
    private AgingType todayAgingType;

    @OneToMany(mappedBy = "checkup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Result> results;

    public void update(
            User user,
            final LocalDate date,
            final List<Meal> meals,
            final String memo
    ) {
        this.user = user;
        this.date = date;
        if (this.meals == null) {
            this.meals = new ArrayList<>();  // 여기서 초기화
        }
        this.meals.clear();
        for (Meal meal : meals) {
            meal.setCheckup(this);
            this.meals.add(meal);
        }
        this.memo = memo;
        this.checkupStatus = CheckupStatus.IN_PROGRESS;
    }

    public void submit(
            User user,
            final LocalDate date,
            final List<Meal> meals,
            final String memo
    ) {
        this.user = user;
        this.date = date;
        if (this.meals == null) {
            this.meals = new ArrayList<>();  // 여기서 초기화
        }
        this.meals.clear();
        if (meals != null) {
            for (Meal meal : meals) {
                meal.setCheckup(this);
                this.meals.add(meal);
            }
        }
        this.memo = memo;
        this.checkupStatus = CheckupStatus.COMPLETED;
    }

    public int getMealNum(MenuType type) {
        int num = 0;
        for (Meal meal : this.getMeals()) {
            if (meal.getMenuType() == type)
                num++;
        };
        return num;
    }

    public void setScore(int totalScore) {
        AgingType todayAgingType;
        if (totalScore < 4) todayAgingType = AgingType.DANGER;
        else if (totalScore < 7) todayAgingType = AgingType.CAUTION;
        else todayAgingType = AgingType.PROPER;

        this.todayAgingType = todayAgingType;
    }
}