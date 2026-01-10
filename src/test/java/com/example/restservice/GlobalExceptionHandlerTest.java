package com.example.restservice;

import com.example.restservice.exception.ApiException;
import com.example.restservice.exception.ErrorResponse;
import com.example.restservice.exception.GlobalExceptionHandler;
import com.example.restservice.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void testApiExceptionHandling() {
        // Given
        ApiException apiException = new ApiException(HttpStatus.NOT_FOUND, "User not found",
                String.valueOf(HttpStatus.NOT_FOUND.value()));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users/999");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleApiException(apiException, request);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("User not found", errorResponse.getMessage());
        assertEquals("/users/999", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
        assertNull(errorResponse.getErrors()); // No field errors for basic ApiException
    }

    @Test
    void testApiExceptionHandlingWithFieldErrors() {
        // Given
        List<ErrorResponse.FieldError> fieldErrors = List.of(
                new ErrorResponse.FieldError("NotBlank", "Name is required", "name"),
                new ErrorResponse.FieldError("Email", "Invalid email format", "email"));
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST, "Validation failed", "VALIDATION_ERROR",
                fieldErrors);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleApiException(apiException, request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("Validation failed", errorResponse.getMessage());
        assertEquals("/users", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());

        // Check field errors
        List<ErrorResponse.FieldError> errors = errorResponse.getErrors();
        assertNotNull(errors);
        assertEquals(2, errors.size());

        // Check first error
        ErrorResponse.FieldError firstError = errors.get(0);
        assertEquals("name", firstError.getField());
        assertEquals("Name is required", firstError.getMessage());
        assertEquals("NotBlank", firstError.getCode());

        // Check second error
        ErrorResponse.FieldError secondError = errors.get(1);
        assertEquals("email", secondError.getField());
        assertEquals("Invalid email format", secondError.getMessage());
        assertEquals("Email", secondError.getCode());
    }

    @Test
    void testGlobalExceptionHandling() {
        // Given
        RuntimeException runtimeException = new RuntimeException("Unexpected error");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGlobalException(runtimeException, request);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("An unexpected error occurred", errorResponse.getMessage());
        assertEquals("/users", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void testValidationExceptionHandling() throws NoSuchMethodException {
        // Given
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "user");
        bindingResult.addError(
                new FieldError("user", "name", null, false, new String[] { "NotBlank" }, null, "Name is required"));
        bindingResult.addError(new FieldError("user", "email", null, false, new String[] { "Email" }, null,
                "Email format is invalid"));

        // Create MethodParameter for the userParam in putUser method
        java.lang.reflect.Method method = UserController.class.getMethod("putUser", User.class, String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        MethodArgumentNotValidException validationException = new MethodArgumentNotValidException(methodParameter,
                bindingResult);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/users");

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationExceptions(validationException,
                request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("Validation failed for 2 field(s)", errorResponse.getMessage());
        assertEquals("/users", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());

        // Check the errors array
        List<ErrorResponse.FieldError> errors = errorResponse.getErrors();
        assertNotNull(errors);
        assertEquals(2, errors.size());

        // Check first error
        ErrorResponse.FieldError firstError = errors.get(0);
        assertEquals("name", firstError.getField());
        assertEquals("Name is required", firstError.getMessage());
        assertEquals("NotBlank", firstError.getCode()); // Default code for FieldError

        // Check second error
        ErrorResponse.FieldError secondError = errors.get(1);
        assertEquals("email", secondError.getField());
        assertEquals("Email format is invalid", secondError.getMessage());
        assertEquals("Email", secondError.getCode()); // Email validation code
    }
}