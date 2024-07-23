package com.example.wellnesscoach.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Checkup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    private Date date;

    private Integer aging;

    private String breakfastMenu;

    private String lunchMenu;

    private String dinnerMenu;

    private String snackMenu;

    private String drinkMenu;

    private String memo;

    @Enumerated(EnumType.STRING)
    private CheckupStatus checkupStatus;

    public Checkup() {

    }

}
