package com.example.wellnesscoach.checkup;

import com.example.wellnesscoach.entity.User;
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
    private List<MenuItem> menuItems;

    private Integer aging;

    private String memo;

    @Enumerated(EnumType.STRING)
    private CheckupStatus checkupStatus;

    @Builder
    public Checkup(User user, LocalDate date, List<MenuItem> menuItems, Integer aging, String memo, CheckupStatus checkupStatus) {
        this.user = user;
        this.date = date;
        this.menuItems = menuItems;
        this.aging = aging;
        this.memo = memo;
        this.checkupStatus = checkupStatus;
    }

    public void update(
            User user,
            final LocalDate date,
            final List<MenuItem> menuItems,
            final String memo
    ) {
        this.user = user;
        this.date = date;
        if (this.menuItems == null) {
            this.menuItems = new ArrayList<>();  // 여기서 초기화
        }
        this.menuItems.clear();
        if (menuItems != null) {
            for (MenuItem menuItem : menuItems) {
                menuItem.setCheckup(this);
                this.menuItems.add(menuItem);
            }
        }
        this.memo = memo;
        this.checkupStatus = CheckupStatus.IN_PROGRESS;
    }
}