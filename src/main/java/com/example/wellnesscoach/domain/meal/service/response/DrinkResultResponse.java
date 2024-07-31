package com.example.wellnesscoach.domain.meal.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DrinkResultResponse {
    private String menuName;
    private boolean sugar;
    private String solution;
    private String targetProductName;
    private String productName;
    private String productLink;
}