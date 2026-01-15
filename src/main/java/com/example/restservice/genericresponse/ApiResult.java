package com.example.restservice.genericresponse;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Generic API response wrapper for single entity responses.
 * Used to wrap single data objects returned from API endpoints.
 * Null values are excluded from JSON serialization.
 * 
 * @param <T> the type of the wrapped value
 * @author Application Development Team
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResult<T>(
                @Schema(description = "Single record") T value) {

}
