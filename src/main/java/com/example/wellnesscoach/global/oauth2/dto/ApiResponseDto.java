package com.example.wellnesscoach.global.oauth2.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponseDto<T> {

    private int status;
    private String code;
    private T message;

    @Builder
    private ApiResponseDto(int status, String code, T message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static <T> ApiResponseDto<T> success(int status, String code, T message) {
        return ApiResponseDto.<T>builder()
                .status(status)
                .code(code)
                .message(message)
                .build();
    }

    public static <T> ApiResponseDto<T> error(int status, String code, String message) {
        return ApiResponseDto.<T>builder()
                .status(status)
                .code(code)
                .message((T) message)
                .build();
    }
}