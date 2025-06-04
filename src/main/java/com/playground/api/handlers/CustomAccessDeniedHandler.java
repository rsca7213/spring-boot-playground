package com.playground.api.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.Exception;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // Generate an Exception response
        Exception exception = new Exception(
                "You are not authorized to access this resource",
                ErrorCode.INVALID_AUTHORIZATION,
                HttpStatus.FORBIDDEN
        );

        // Generate the Output Response
        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, exception);
        out.flush();
    }
}
