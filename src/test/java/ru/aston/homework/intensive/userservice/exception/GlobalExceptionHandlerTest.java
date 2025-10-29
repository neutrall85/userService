package ru.aston.homework.intensive.userservice.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleValidationExceptions_ShouldReturnValidationErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = Arrays.asList(
                new FieldError("user", "name", "Name is required"),
                new FieldError("user", "email", "Email must be valid")
        );
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(fieldErrors.stream().map(error -> (org.springframework.validation.ObjectError) error).toList());
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleValidationExceptions(exception);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.get("status"));
        assertEquals("Validation Failed", body.get("error"));
        assertEquals("Please check the following fields", body.get("message"));
        Map<String, String> errors = (Map<String, String>) body.get("errors");
        assertNotNull(errors);
        assertEquals("Name is required", errors.get("name"));
        assertEquals("Email must be valid", errors.get("email"));
    }

    @Test
    void handleHttpMessageNotReadable_WithAgeError_ShouldReturnAgeSpecificMessage() {
 
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        when(exception.getMessage()).thenReturn("JSON parse error: Cannot deserialize value of type `int` from String \"abc\": not a valid Integer value for age");
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleHttpMessageNotReadable(exception);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Age must be a valid number", body.get("message"));
        assertEquals("Invalid Request Body", body.get("error"));
        assertEquals("Check that all fields have correct data types", body.get("detail"));
    }

    @Test
    void handleHttpMessageNotReadable_WithIntegerError_ShouldReturnIntegerSpecificMessage() {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        when(exception.getMessage()).thenReturn("JSON parse error: Cannot deserialize value of type `java.lang.Integer`");
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleHttpMessageNotReadable(exception);
        assertNotNull(response);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Numeric field contains invalid value", body.get("message"));
    }

    @Test
    void handleHttpMessageNotReadable_WithJSONParseError_ShouldReturnJSONSpecificMessage() {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        when(exception.getMessage()).thenReturn("JSON parse error: Unexpected character");
        var response = exceptionHandler.handleHttpMessageNotReadable(exception);
        assertNotNull(response);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Invalid JSON format in request body", body.get("message"));
    }

    @Test
    void handleHttpMessageNotReadable_WithNullMessage_ShouldReturnDefaultMessage() {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        when(exception.getMessage()).thenReturn(null);
        var response = exceptionHandler.handleHttpMessageNotReadable(exception);
        assertNotNull(response);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Invalid JSON format", body.get("message"));
    }

    @Test
    void handleHttpMessageNotReadable_WithGenericMessage_ShouldReturnDefaultMessage() {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        when(exception.getMessage()).thenReturn("Some other error");
        var response = exceptionHandler.handleHttpMessageNotReadable(exception);
        assertNotNull(response);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Invalid JSON format", body.get("message"));
    }

    @Test
    void handleMethodArgumentTypeMismatch_WithKnownType_ShouldReturnTypeInfo() {
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("id");
        when(exception.getRequiredType()).thenReturn((Class) Integer.class);
        var response = exceptionHandler.handleMethodArgumentTypeMismatch(exception);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Invalid Argument Type", body.get("error"));
        assertEquals("Parameter 'id' should be of type Integer", body.get("message"));
    }

    @Test
    void handleMethodArgumentTypeMismatch_WithNullType_ShouldReturnUnknownType() {
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("param");
        when(exception.getRequiredType()).thenReturn(null);
        var response = exceptionHandler.handleMethodArgumentTypeMismatch(exception);
        assertNotNull(response);
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Parameter 'param' should be of type unknown", body.get("message"));
    }

    @Test
    void handleMissingServletRequestParameter_ShouldReturnMissingParameterMessage() {
        MissingServletRequestParameterException exception =
                new MissingServletRequestParameterException("page", "int");
        var response = exceptionHandler.handleMissingParams(exception);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Missing Parameter", body.get("error"));
        assertEquals("Required parameter 'page' is missing", body.get("message"));
    }

    @Test
    void handleIllegalArgumentException_ShouldReturnValidationError() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");
        var response = exceptionHandler.handleIllegalArgumentException(exception);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Validation Error", body.get("error"));
        assertEquals("Invalid argument", body.get("message"));
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() {
        Exception exception = new Exception("Unexpected error");
        var response = exceptionHandler.handleGenericException(exception);
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals("An unexpected error occurred", body.get("message"));
    }
}
