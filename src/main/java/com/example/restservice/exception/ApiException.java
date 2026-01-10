package com.example.restservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String message;
    private final String code;

    /**
     * List of field-specific errors associated with this exception.
     * Each FieldError contains details about validation failures or specific field
     * issues.
     * This corresponds to the ErrorResponse.FieldError structure used in API error
     * responses.
     */
    private final List<ErrorResponse.FieldError> errors;

    public ApiException(HttpStatus status, String message, String code) {
        super(message);
        this.status = status;
        this.message = message;
        this.code = code;
        this.errors = null;
    }

    public ApiException(HttpStatus status, String message, String code, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.message = message;
        this.code = code;
        this.errors = null;
    }

    public ApiException(HttpStatus status, String message, String code, List<ErrorResponse.FieldError> errors,
            Throwable cause) {
        super(message, cause);
        this.status = status;
        this.message = message;
        this.code = code;
        this.errors = errors;
    }
}