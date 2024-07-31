package com.example.wellnesscoach.domain.checkup.repository;

import com.example.wellnesscoach.domain.checkup.Checkup;
import com.example.wellnesscoach.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CheckupRepository extends JpaRepository<Checkup, Long> {
    Checkup findByDate(LocalDate date);

    List<Checkup> findAllCheckupsByUser(User user);
}