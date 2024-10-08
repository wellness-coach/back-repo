package com.example.wellnesscoach.service;

import com.example.wellnesscoach.domain.Recommendation;
import com.example.wellnesscoach.domain.Scrap;
import com.example.wellnesscoach.repository.ScrapRepoisitory;
import com.example.wellnesscoach.domain.User;
import com.example.wellnesscoach.global.CustomException;
import com.example.wellnesscoach.global.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class ScrapService {

    private final ScrapRepoisitory scrapRepoisitory;

    public ScrapService(ScrapRepoisitory scrapRepoisitory) {
        this.scrapRepoisitory = scrapRepoisitory;
    }

    public void addScrap(User user, Recommendation recommendation) {

        if (scrapRepoisitory.findByUserAndRecommendation(user, recommendation) != null) {
            throw new CustomException(ErrorCode.ALREADY_SCRAPED);
        }

        Scrap scrap = new Scrap();
        scrap.setScrap(user, recommendation);
        scrapRepoisitory.save(scrap);
    }

    public void cancelScrap(User user, Recommendation recommendation) {
        Scrap scrap = scrapRepoisitory.findByUserAndRecommendation(user, recommendation);

        if (scrap == null) {
            throw new CustomException(ErrorCode.SCRAP_NOT_FOUND);
        }

        scrapRepoisitory.delete(scrap);
    }
}