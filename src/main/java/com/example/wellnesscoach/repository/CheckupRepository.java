package com.example.wellnesscoach.repository;

import com.example.wellnesscoach.entity.Checkup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckupRepository extends JpaRepository<Checkup, Long> {
}