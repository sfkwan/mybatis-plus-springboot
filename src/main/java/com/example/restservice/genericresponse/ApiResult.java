package com.example.restservice.genericresponse;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResult<T>(
        @Schema(description = "Single record") T value) {

}
