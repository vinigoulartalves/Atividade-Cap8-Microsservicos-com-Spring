package br.com.ecodenuncia.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorItem> errors
) {

    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path, null);
    }

    public static ErrorResponse of(int status, String error, String message, String path, List<FieldErrorItem> errors) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path, errors);
    }

    public record FieldErrorItem(String field, String message) {
    }
}
