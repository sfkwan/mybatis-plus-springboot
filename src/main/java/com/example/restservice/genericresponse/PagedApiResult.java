package com.example.restservice.genericresponse;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Generic API response wrapper for paginated data.
 * Contains pagination metadata along with the list of records.
 * Null values are excluded from JSON serialization.
 * 
 * @param <T> the type of the paginated data list
 * @author Application Development Team
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PagedApiResult<T>(
                @Schema(description = "Total number of records matching the query", example = "25") Long total,
                @Schema(description = "Total number of pages", example = "3") Long totalPages,
                @Schema(description = "Number of records in current page", example = "10") Integer pageSize,
                @Schema(description = "List of records in current page") T value) {

}
