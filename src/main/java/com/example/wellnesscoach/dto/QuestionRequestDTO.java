package com.example.wellnesscoach.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionRequestDTO {
    private String question;

    public QuestionRequestDTO(String question) {
        this.question = question;
    }
}