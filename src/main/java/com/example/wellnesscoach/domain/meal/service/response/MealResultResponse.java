package com.example.wellnesscoach.domain.meal.service.response;

import com.example.wellnesscoach.domain.recommendation.service.ProductResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class MealResultResponse {
    private String menuName;
    private boolean sugar;
    private boolean grain;
    private boolean redmeat;
    private boolean salt;
    private String solution;
    ProductResponse productResponse;
}