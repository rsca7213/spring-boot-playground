package com.playground.api.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.playground.api.enums.ErrorCode;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private int status;
    private String statusText;
    private String message;
    private LocalDateTime timestamp;
    private ErrorCode errorCode;
    private Map<String, String> errors;
    private String originalException;
}
