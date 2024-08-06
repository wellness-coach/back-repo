package com.example.wellnesscoach.domain.recommendation.repository;

import com.example.wellnesscoach.domain.recommendation.Recommendation;
import com.example.wellnesscoach.domain.recommendation.Scrap;
import com.example.wellnesscoach.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByUser(User user);
}
