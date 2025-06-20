package com.playground.api.exceptions;

import com.playground.api.enums.ErrorCode;
import com.playground.api.utils.EnvironmentUtils;
import com.playground.api.utils.ExceptionHandlingUtils;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Environment environment;

    @Autowired
    public GlobalExceptionHandler(Environment environment) {
        this.environment = environment;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleException(ApiException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(ex.getStatusCode().value())
                .statusText(ex.getStatusCode().getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .errorCode(ex.getErrorCode())
                .build();

        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((fieldError) -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors().forEach((globalError) -> errors.put(globalError.getObjectName(), globalError.getDefaultMessage()));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .statusText(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .message("Invalid parameters where provided")
                .errorCode(ErrorCode.INVALID_PARAMETERS)
                .errors(errors)
                .build();


        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1); // Extract param name
            errors.put(fieldName, violation.getMessage()); // Use the default message from the annotation
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .statusText(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .message("One or more path or query parameters are invalid")
                .errorCode(ErrorCode.INVALID_PARAMETERS)
                .errors(errors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        String message = ExceptionHandlingUtils.getHttpMessageNotReadableMessage(cause);
        Map<String, String> errors = ExceptionHandlingUtils.getHttpMessageNotReadableErrors(cause);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .statusText(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)
                .errorCode(ErrorCode.INVALID_PARAMETERS)
                .errors(errors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .statusText(HttpStatus.FORBIDDEN.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .message("Access to this resource is denied")
                .errorCode(ErrorCode.INVALID_AUTHORIZATION)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        String exceptionClass = EnvironmentUtils.isDevelopment(environment) ? ex.getClass().getName() : null;
        String stackTrace = EnvironmentUtils.isDevelopment(environment) ? ExceptionHandlingUtils.getStackTraceAsString(ex) : null;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .statusText(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .message("An unexpected server error occurred. Please try again later")
                .errorCode(ErrorCode.RUNTIME_ERROR)
                .originalException(exceptionClass)
                .stackTrace(stackTrace)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
