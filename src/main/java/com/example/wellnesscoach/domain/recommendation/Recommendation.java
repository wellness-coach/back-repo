package com.example.wellnesscoach.domain.recommendation;

import com.example.wellnesscoach.domain.meal.Meal;
import com.example.wellnesscoach.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "mealId")
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    private String targetIngredient;

    private String productName;

    private String productLink;

    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scrap> scraps;

    public void createRecommendation(
            Meal meal,
            final String targetIngredient,
            final String productName,
            final String productLink,
            User user
    ) {
        this.meal = meal;
        this.targetIngredient = targetIngredient;
        this.productName = productName;
        this.productLink = productLink;
        this.user = user;
    }
}