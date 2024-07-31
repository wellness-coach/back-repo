package com.example.wellnesscoach.domain.result.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScoreResponse {
    private Integer score;

    public ScoreResponse(Integer score) {
        this.score = score;
    }
}