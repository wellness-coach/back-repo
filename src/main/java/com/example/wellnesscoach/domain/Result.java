package com.example.wellnesscoach.domain;

import com.example.wellnesscoach.domain.enums.MenuType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "checkupId")
    private Checkup checkup;

    @Enumerated(EnumType.STRING)
    private MenuType menuType;

    private Integer score;


    public void saveResult(
            Checkup checkup,
            MenuType menuType,
            Integer score
    ){
        this.checkup = checkup;
        this.menuType = menuType;
        this.score = score;
    }
}
