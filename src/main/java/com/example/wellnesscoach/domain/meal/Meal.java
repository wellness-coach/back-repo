package com.example.wellnesscoach.domain.meal;

import com.example.wellnesscoach.domain.checkup.Checkup;
import com.example.wellnesscoach.domain.recommendation.Recommendation;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkup_id")
    private Checkup checkup;

    @Enumerated(EnumType.STRING)
    private MenuType menuType;

    private String menuName;

    @Enumerated(EnumType.STRING)
    private AgingType agingType; //저속노화인지 가속노화인지

    private Boolean sugar;

    private Boolean grain;

    private Boolean redmeat;

    private Boolean carbohydrate;

    private Integer score;

    private String solution;

    @OneToOne(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Recommendation recommendation;

    public void updateMeal(
            Checkup checkup,
            final MenuType menuType,
            final String menuName,
            final AgingType agingType,
            final Boolean sugar,
            final Boolean grain,
            final Boolean redmeat,
            final Boolean carbohydrate,
            final Integer score,
            final String solution
    ) {
        this.checkup = checkup;
        this.menuType = menuType;
        this.menuName = menuName;
        this.agingType = agingType;
        this.sugar = sugar;
        this.grain = grain;
        this.redmeat = redmeat;
        this.carbohydrate = carbohydrate;
        this.score = score;
        this.solution = solution;
    }

    public void setCheckup(Checkup checkup) {
        this.checkup = checkup;
    }
    // Getters and setters
}