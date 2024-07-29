package com.example.wellnesscoach.domain.checkup.exception;

public abstract class CheckupException extends RuntimeException{

    public CheckupException(final String message) {
        super(message);
    }
}
