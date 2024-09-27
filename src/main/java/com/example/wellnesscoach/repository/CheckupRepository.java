package com.example.wellnesscoach.repository;

import com.example.wellnesscoach.domain.Checkup;
import com.example.wellnesscoach.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CheckupRepository extends JpaRepository<Checkup, Long> {
    Checkup findByDate(LocalDate date);

    List<Checkup> findAllCheckupsByUser(User user);

    @Query("SELECT c FROM Checkup c WHERE c.user = :user AND c.date BETWEEN :startDate AND :endDate")
    List<Checkup> findCheckupsByUserAndDateRange(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    Checkup findByUserAndDate(User user, LocalDate date);
}