package com.playground.api.exceptions;

import com.playground.api.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private int status;
    private String statusText;
    private String message;
    private LocalDateTime timestamp;
    private ErrorCode errorCode;
}
