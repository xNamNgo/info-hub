package com.info_hub.exceptions;

public class BadRequestException extends RuntimeException{
    private String message;

    public BadRequestException(String message) {
        super(message);
    }
}
