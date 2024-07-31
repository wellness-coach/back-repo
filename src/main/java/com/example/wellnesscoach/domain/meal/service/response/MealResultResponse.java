package com.example.wellnesscoach.domain.meal.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MealResultResponse {
    private String menuName;
    private boolean sugar;
    private boolean grain;
    private boolean redmeat;
    private boolean carbohydrate;
    private String solution;
    private String targetProductName;
    private String productName;
    private String productLink;
}