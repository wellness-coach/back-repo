package com.example.wellnesscoach.domain.recommendation;

import com.example.wellnesscoach.domain.meal.Meal;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id")
    private Meal meal;

    private String targetIngredient;

    private String productName;

    private String productLink;

    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scrap> scraps;

    public void createRecommendation(
            Meal meal,
            final String targetIngredient,
            final String productName,
            final String productLink
    ) {
        this.meal = meal;
        this.targetIngredient = targetIngredient;
        this.productName = productName;
        this.productLink = productLink;
    }
}