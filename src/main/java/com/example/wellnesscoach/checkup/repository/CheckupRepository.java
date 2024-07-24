package com.example.wellnesscoach.checkup.repository;

import com.example.wellnesscoach.checkup.Checkup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckupRepository extends JpaRepository<Checkup, Long> {
}