package com.example.wellnesscoach.domain.meal.service.response;

import com.example.wellnesscoach.domain.recommendation.service.ProductResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DrinkResultResponse {
    private String menuName;
    private boolean sugar;
    private String solution;
    ProductResponse productResponse;
}