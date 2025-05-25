package com.playground.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now(),
                ex.getErrorCode()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnsupportedException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedException(UnsupportedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_IMPLEMENTED.value(),
                HttpStatus.NOT_IMPLEMENTED.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now(),
                ex.getErrorCode()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_IMPLEMENTED);
    }

}
