package com.example.wellnesscoach.exception;

import com.example.wellnesscoach.dto.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.validation.BindingResult;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ApiResponseDto<String>> handleException(Exception e) {
        log.error("Exception: ", e);
        ApiResponseDto<String> response = ApiResponseDto.error(HttpStatus.BAD_REQUEST.value(), "E001", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    protected ResponseEntity<ApiResponseDto<String>> handleNoSuchElementException(NoSuchElementException e) {
        log.error("NoSuchElementException: ", e);
        ApiResponseDto<String> response = ApiResponseDto.error(HttpStatus.NO_CONTENT.value(), "E002", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ApiResponseDto<String>> handleCustomException(CustomException e) {
        log.error("CustomException: ", e);
        CustomErrorCode errorCode = e.getErrorCode();
        ApiResponseDto<String> response = ApiResponseDto.error(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ApiResponseDto<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: ", e);
        BindingResult bindingResult = e.getBindingResult();
        String message = bindingResult.hasErrors() ? bindingResult.getAllErrors().get(0).getDefaultMessage() : "Validation error";
        ApiResponseDto<String> response = ApiResponseDto.error(HttpStatus.BAD_REQUEST.value(), "E003", message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}