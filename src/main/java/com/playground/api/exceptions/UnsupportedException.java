package com.playground.api.exceptions;

import com.playground.api.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
@Getter
public class UnsupportedException extends RuntimeException {
    private final ErrorCode errorCode;

    public UnsupportedException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
