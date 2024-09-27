package com.example.wellnesscoach.service.response;

import lombok.Builder;
import lombok.Getter;

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