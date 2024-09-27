package com.example.wellnesscoach.service.response;

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