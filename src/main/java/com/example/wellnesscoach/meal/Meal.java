package com.example.wellnesscoach.meal;

import com.example.wellnesscoach.checkup.MenuItem;
import jakarta.persistence.*;

@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private MenuItem menuItem;

    private String menuName;

    @Enumerated(EnumType.STRING)
    private AgingType agingType; //저속노화인지 가속노화인지

    private Boolean sugar;

    private Boolean grain;

    private Boolean redmeat;

    private Boolean carbohydrate;

    private Integer score;

    private String solution;


    public void updateMeal(
            MenuItem menuItem,
            final String menuName,
            final AgingType agingType,
            final Boolean sugar,
            final Boolean grain,
            final Boolean redmeat,
            final Boolean carbohydrate,
            final Integer score,
            final String solution
    ) {
        this.menuItem = menuItem;
        this.menuName = menuName;
        this.agingType = agingType;
        this.sugar = sugar;
        this.grain = grain;
        this.redmeat = redmeat;
        this.carbohydrate = carbohydrate;
        this.score = score;
        this.solution = solution;
    }
}
