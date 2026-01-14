package com.example.restservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        log.error("API Exception: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(ZonedDateTime.now());
        HttpStatus status = ex.getHttpStatus() != null ? ex.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
        errorResponse.setCode(ex.getCode() != null ? ex.getCode() : status.toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setError(status.getReasonPhrase());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setErrors(ex.getErrors());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        log.error("Validation Exception: {}", ex.getMessage(), ex);

        List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {

            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            String errorCode = error.getCode() != null ? error.getCode() : "VALIDATION_ERROR";

            ErrorResponse.FieldError fieldError = new ErrorResponse.FieldError();
            fieldError.setCode(errorCode);
            fieldError.setMessage(errorMessage);
            fieldError.setField(fieldName);
            fieldErrors.add(fieldError);
        });

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(ZonedDateTime.now());
        errorResponse.setCode(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setErrors(fieldErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handles @Validated on query/path params
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {
        List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        log.error("Constraint Violation Exception: {}", ex, ex);
        ex.getConstraintViolations().forEach(violation -> {

            ErrorResponse.FieldError fieldError = new ErrorResponse.FieldError();
            fieldError.setField(violation.getPropertyPath().toString());
            fieldError.setMessage(violation.getMessage());
            fieldError.setCode("400");
            fieldErrors.add(fieldError);

        });

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(ZonedDateTime.now());
        errorResponse.setCode(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setErrors(fieldErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(ZonedDateTime.now());
        errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorResponse.setMessage("An unexpected error occurred");
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}