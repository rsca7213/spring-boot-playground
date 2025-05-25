package com.playground.api.exceptions;

import com.playground.api.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Exception extends RuntimeException {
    private final ErrorCode errorCode;
    private final HttpStatus statusCode;

    public Exception(String message, ErrorCode errorCode, HttpStatus statusCode) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }
}
