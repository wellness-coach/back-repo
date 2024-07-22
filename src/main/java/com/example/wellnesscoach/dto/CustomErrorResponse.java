package com.example.wellnesscoach.dto;

import com.board.demo.exception.CustomErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;


@Getter
public class CustomErrorResponse {
    private int status;
    private String code;
    private String message;

    @Builder
    private CustomErrorResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static CustomErrorResponse of(CustomErrorCode customErrorCode){
        return CustomErrorResponse.builder()
                .status(customErrorCode.getStatus())
                .code(customErrorCode.getCode())
                .message(customErrorCode.getMessage())
                .build();
    }

    public static CustomErrorResponse of(HttpStatus status, String message) {
        return CustomErrorResponse.builder()
                .status(status.value())
                .message(message)
                .build();
    }

    public static CustomErrorResponse of(BindingResult bindingResult) {
        String message = "";

        if (bindingResult.hasErrors()) {
            message = bindingResult.getAllErrors().get(0).getDefaultMessage();
        }
        return CustomErrorResponse.of(HttpStatus.BAD_REQUEST, message);
    }
}