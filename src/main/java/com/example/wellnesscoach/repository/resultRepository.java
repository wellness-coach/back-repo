package com.example.wellnesscoach.repository;

import com.example.wellnesscoach.domain.Checkup;
import com.example.wellnesscoach.domain.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface resultRepository extends JpaRepository<Result, Long> {

    List<Result> findByCheckup(Checkup checkup);



}
