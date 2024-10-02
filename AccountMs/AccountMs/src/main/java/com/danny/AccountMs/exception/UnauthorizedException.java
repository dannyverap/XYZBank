package com.danny.AccountMs.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnauthorizedException extends RuntimeException {
    private final int statusCode;

    public UnauthorizedException(String message) {
        super(message);
        this.statusCode = HttpStatus.UNAUTHORIZED.value();
    }
}
