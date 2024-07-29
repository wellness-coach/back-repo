package com.example.wellnesscoach.domain.chatGPT.checkup.repository;

import com.example.wellnesscoach.domain.chatGPT.checkup.Checkup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckupRepository extends JpaRepository<Checkup, Long> {
}