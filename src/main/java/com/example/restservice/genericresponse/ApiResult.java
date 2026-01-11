package com.example.restservice.genericresponse;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResult<T>(
        @Schema(description = "List of records in current page") T value,
        @Nullable @Schema(description = "Total number of records matching the query", example = "25") Long total,
        @Nullable @Schema(description = "Total number of pages", example = "3") Long totalPages,
        @Nullable @Schema(description = "Number of records in current page", example = "10") Integer noOfRecordsInCurrentPage) {

    public ApiResult(T value) {
        this(value, null, null, null);
    }
}
