package com.example.wellnesscoach.domain.recommendation;

import com.example.wellnesscoach.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long scrapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendId")
    private Recommendation recommendation;

    public void setScrap(User user, Recommendation recommendation) {
        this.user = user;
        this.recommendation = recommendation;
    }
}
