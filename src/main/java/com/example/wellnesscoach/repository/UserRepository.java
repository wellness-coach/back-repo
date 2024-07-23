package com.example.wellnesscoach.repository;

import com.example.wellnesscoach.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
