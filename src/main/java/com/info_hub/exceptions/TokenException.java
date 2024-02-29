package com.info_hub.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TokenException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public TokenException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public TokenException(String message) {
        super(message);
        this.message = message;
    }
}
