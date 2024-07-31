package com.example.wellnesscoach.domain.recommendation.service;

import com.example.wellnesscoach.domain.recommendation.Recommendation;
import com.example.wellnesscoach.domain.recommendation.Scrap;
import com.example.wellnesscoach.domain.recommendation.repository.ScrapRepoisitory;
import com.example.wellnesscoach.domain.user.User;
import org.springframework.stereotype.Service;

@Service
public class ScrapService {

    private final ScrapRepoisitory scrapRepoisitory;

    public ScrapService(ScrapRepoisitory scrapRepoisitory) {
        this.scrapRepoisitory = scrapRepoisitory;
    }

    public void addScrap(User user, Recommendation recommendation) {
        Scrap scrap = new Scrap();
        scrap.setScrap(user, recommendation);
        scrapRepoisitory.save(scrap);
    }

    public void cancelScrap(User user, Recommendation recommendation) {
        Scrap scrap = scrapRepoisitory.findByUserAndRecommendation(user, recommendation);
        scrapRepoisitory.delete(scrap);
    }
}