package com.example.restservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(description = "The timestamp when the error occurred", example = "2024-01-11 10:30:00 UTC", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss z")
    private ZonedDateTime timestamp;

    /**
     * The HTTP status code of the error (e.g., 400, 404, 500).
     */
    @Schema(description = "HTTP status code", example = "400", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    /**
     * A short description of the error type (e.g., "Bad Request", "Not Found").
     */
    @Schema(description = "Error type description", example = "Bad Request", requiredMode = Schema.RequiredMode.REQUIRED)
    private String error;

    /**
     * A detailed message describing the error.
     */
    @Schema(description = "Detailed error message", example = "Validation failed for one or more fields", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    /**
     * The request path that caused the error.
     */
    @Schema(description = "Request path that caused the error", example = "/api/users", requiredMode = Schema.RequiredMode.REQUIRED)
    private String path;

    /**
     * A list of field-level validation errors, if any.
     * This field is omitted from JSON serialization if null.
     */
    @Schema(description = "List of field validation errors", nullable = true)
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
        @Schema(description = "Error code for the validation failure", example = "NotBlank")
        private String code;

        /**
         * A descriptive message explaining the validation error.
         */
        @Schema(description = "Descriptive message explaining the validation error", example = "must not be blank")
        private String message;

        /**
         * The name of the field that failed validation.
         */
        @Schema(description = "Name of the field that failed validation", example = "email")
        private String field;
    }
}