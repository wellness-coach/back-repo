package com.example.wellnesscoach.domain.user.repository;

import com.example.wellnesscoach.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
