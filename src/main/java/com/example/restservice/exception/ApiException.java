package com.example.restservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Custom exception class for API errors.
 * This exception is used throughout the application to handle business logic
 * errors
 * and provide structured error responses with HTTP status codes, error codes,
 * and
 * optional field-level validation errors.
 * 
 * @author Application Development Team
 * @since 1.0
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApiException extends RuntimeException {
    private final HttpStatus httpStatus;
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

    /**
     * Constructs an ApiException with HTTP status, message, and error code.
     * 
     * @param httpStatus the HTTP status code for this exception
     * @param message    the error message to be displayed
     * @param code       the application-specific error code for categorizing errors
     */
    public ApiException(HttpStatus httpStatus, String message, String code) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
        this.errors = null;
    }

    /**
     * Constructs an ApiException with HTTP status, message, error code, and root
     * cause.
     * Useful for wrapping checked exceptions with additional context.
     * 
     * @param httpStatus the HTTP status code for this exception
     * @param message    the error message to be displayed
     * @param code       the application-specific error code for categorizing errors
     * @param cause      the underlying exception that caused this ApiException
     */
    public ApiException(HttpStatus httpStatus, String message, String code, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
        this.errors = null;
    }

    /**
     * Constructs an ApiException with HTTP status, message, error code,
     * field-specific errors, and root cause.
     * Primarily used for validation errors where individual fields have specific
     * error messages.
     * 
     * @param httpStatus the HTTP status code for this exception
     * @param message    the error message to be displayed
     * @param code       the application-specific error code for categorizing errors
     * @param errors     a list of field-specific errors containing details about
     *                   validation failures
     * @param cause      the underlying exception that caused this ApiException
     */
    public ApiException(HttpStatus httpStatus, String message, String code, List<ErrorResponse.FieldError> errors,
            Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
        this.errors = errors;
    }
}