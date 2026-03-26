package com.miuky.ewallet.dto.response;

import com.miuky.ewallet.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
    public static ErrorResponse build(ErrorCode code, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(code.getHttpStatus().value())
                .error(code.getHttpStatus().toString())
                .message(code.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    public static ErrorResponse build(int value, String error, String message, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(value)
                .error(error)
                .message(message)
                .path(request.getRequestURI())
                .build();
    }
}
