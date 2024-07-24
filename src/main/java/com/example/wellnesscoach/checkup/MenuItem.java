package com.example.wellnesscoach.checkup;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkup_id")
    private Checkup checkup;

    @Enumerated(EnumType.STRING)
    private MenuType type;

    private String name;

    public MenuItem(Checkup checkup, MenuType type, String name) {
        this.checkup = checkup;
        this.type = type;
        this.name = name;
    }

    public void setCheckup(Checkup checkup) {
        this.checkup = checkup;
    }

}
