package com.example.restservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represents a standardized error response for API exceptions.
 * This class is used to structure error information returned to clients
 * when an API request fails, providing details such as timestamp, status code,
 * error type, message, request path, and optional field-level validation
 * errors.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * The timestamp when the error occurred, formatted as "yyyy-MM-dd HH:mm:ss z".
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss z")
    private ZonedDateTime timestamp;

    /**
     * The HTTP status code of the error (e.g., 400, 404, 500).
     */
    private int status;

    /**
     * A short description of the error type (e.g., "Bad Request", "Not Found").
     */
    private String error;

    /**
     * A detailed message describing the error.
     */
    private String message;

    /**
     * The request path that caused the error.
     */
    private String path;

    /**
     * A list of field-level validation errors, if any.
     * This field is omitted from JSON serialization if null.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FieldError> errors;

    /**
     * Represents a field-level validation error.
     * Contains details about a specific field that failed validation.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The error code for the field validation failure.
         */
        private String code;

        /**
         * A descriptive message explaining the validation error.
         */
        private String message;

        /**
         * The name of the field that failed validation.
         */
        private String field;
    }
}