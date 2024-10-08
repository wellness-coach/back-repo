package com.example.wellnesscoach.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //Global error
    //400 BAD_REQUEST: 잘못된 요청
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "G001", "잘못된 요청입니다."),

    //404 NOT_FOUND: 리소스를 찾을 수 없음
    POSTS_NOT_FOUND(HttpStatus.NOT_FOUND, "G002","리소스를 찾을 수 없습니다."),

    //405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "G003", "허용되지 않은 메서드입니다."),

    //500 INTERNAL_SERVER_ERROR: 내부 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G004", "내부 서버 오류입니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "유저 정보를 찾을 수 없습니다."),

    SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, "C002", "스크랩 되지 않은 제품입니다."),

    CHECKUP_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "리포트 정보를 찾을 수 없습니다."),

    CHECKUP_ALREADY_EXISTS(HttpStatus.CONFLICT, "C004", "해당 날짜에 이미 진단지가 존재합니다."),

    ALREADY_SCRAPED(HttpStatus.CONFLICT, "C005", "유저가 이미 스크랩한 추천 제품입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
