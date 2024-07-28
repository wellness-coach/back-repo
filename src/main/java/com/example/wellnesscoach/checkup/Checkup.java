package com.example.wellnesscoach.checkup;

import com.example.wellnesscoach.entity.User;
import com.example.wellnesscoach.meal.Meal;
import jakarta.persistence.*;
import lombok.Builder;
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
    private List<Meal> meals;

    private String memo;

    @Enumerated(EnumType.STRING)
    private CheckupStatus checkupStatus;

    @Builder
    public Checkup(User user, LocalDate date, List<Meal> meals, String memo, CheckupStatus checkupStatus) {
        this.user = user;
        this.date = date;
        this.meals = meals;
        this.memo = memo;
        this.checkupStatus = checkupStatus;
    }

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

    /*public int getMealNum(Checkup checkup, MenuType type) {
        int num = 0;
        for (MenuItem menuItem : checkup.getMenuItems()) {
            if (menuItem.getType() == type)
                num++;
        };
        return num;
    }
*/
}