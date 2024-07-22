package com.example.wellnesscoach.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CustomErrorCode {


    // COMMON
    INVALID_CODE(400, "C001", "Invalid Code"),
    RESOURCE_NOT_FOUND(404, "C002", "Resource not found"),
    EXPIRED_CODE(400, "C003", "Expired Code"),

    // AWS
    AWS_ERROR(500, "A001", "AWS client error"),
    TEMPORARY_SERVER_ERROR(500, "A002", "Temporary server error"),

    // Validation Errors
    UNVALID_ERROR(400, "V001", "작성자가 일치하지 않습니다."),
    NO_TITLE(400, "V001", "타이틀이 비어있습니다."),
    NO_CONTENT(400, "V002", "내용이 비어있습니다."),
    NO_USER(400, "V003", "작성자가 비어있습니다."),
    NO_RESOURCE(400, "V004", "필수 입력값이 누락되었습니다.");


    private final int status;
    private final String code;
    private final String message;

    CustomErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}