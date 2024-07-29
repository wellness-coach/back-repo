package com.example.wellnesscoach.domain.meal.repository;

import com.example.wellnesscoach.domain.meal.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

}
