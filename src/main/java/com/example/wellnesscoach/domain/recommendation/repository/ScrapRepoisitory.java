package com.example.wellnesscoach.domain.recommendation.repository;

import com.example.wellnesscoach.domain.recommendation.Recommendation;
import com.example.wellnesscoach.domain.recommendation.Scrap;
import com.example.wellnesscoach.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public interface ScrapRepoisitory extends JpaRepository<Scrap, Long> {

    Scrap findByUserAndRecommendation(User user, Recommendation recommendation);
}