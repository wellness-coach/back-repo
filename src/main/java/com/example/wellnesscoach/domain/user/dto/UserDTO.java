package com.example.wellnesscoach.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long userId;
    private String role;
    private String name;
    private String username;
}