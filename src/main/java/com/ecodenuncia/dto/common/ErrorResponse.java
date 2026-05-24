package com.ecodenuncia.dto.common;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> details
) {
    public static ErrorResponse of(int status, String error, String message, String path, List<String> details) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path, details);
    }

    public static ErrorResponse of(int status, String error, String message, String path) {
        return of(status, error, message, path, null);
    }
}
