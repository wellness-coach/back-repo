package com.example.wellnesscoach.domain.checkup.repository;

import com.example.wellnesscoach.domain.checkup.Checkup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckupRepository extends JpaRepository<Checkup, Long> {
}