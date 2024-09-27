package com.example.wellnesscoach.repository;

import com.example.wellnesscoach.domain.Recommendation;
import com.example.wellnesscoach.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByUser(User user);
}
