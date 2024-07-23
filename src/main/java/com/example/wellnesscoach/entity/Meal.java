package com.example.wellnesscoach.entity;

import jakarta.persistence.*;

@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealId;

    private String mealType;

    private Boolean sugar;

    private Boolean grain;

    private Boolean carbohydrate;

    private Long score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkupId")
    private Checkup checkup;

}
