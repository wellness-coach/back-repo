package com.example.wellnesscoach.global;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode code;

    public CustomException(ErrorCode code){
        super(code.getMessage());
        this.code = code ;
    }

    public CustomException(ErrorCode code, String message){
        super(message);
        this.code = code ;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public ErrorCode getCode(){
        return this.code;
    }
}
