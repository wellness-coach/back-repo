package com.example.wellnesscoach.domain.recommendation.repository;

import com.example.wellnesscoach.domain.recommendation.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
