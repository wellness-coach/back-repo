package com.example.wellnesscoach.repository;

import com.example.wellnesscoach.domain.Recommendation;
import com.example.wellnesscoach.domain.Scrap;
import com.example.wellnesscoach.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public interface ScrapRepoisitory extends JpaRepository<Scrap, Long> {

    Scrap findByUserAndRecommendation(User user, Recommendation recommendation);
}