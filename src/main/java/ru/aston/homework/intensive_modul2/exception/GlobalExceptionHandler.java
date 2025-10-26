package ru.aston.homework.intensive_modul2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.NOT_FOUND,
                "User Not Found",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.CONFLICT,
                "Email Already Exists",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                "Please check the following fields"
        );

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        errorResponse.put("errors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        var userMessage = getString(ex);

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid Request Body",
                userMessage
        );

        // Добавляем детали для разработчиков (только в development)
        errorResponse.put("detail", "Check that all fields have correct data types");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private static String getString(HttpMessageNotReadableException ex) {
        String message = ex.getMessage();
        String userMessage = "Invalid JSON format";

        // Определяем конкретную причину ошибки
        if (message != null) {
            if (message.contains("age")) {
                userMessage = "Age must be a valid number";
            } else if (message.contains("Integer")) {
                userMessage = "Numeric field contains invalid value";
            } else if (message.contains("JSON parse error")) {
                userMessage = "Invalid JSON format in request body";
            }
        }
        return userMessage;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>>
        handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Class<?> requiredType = ex.getRequiredType();
        String typeName = "unknown";

        if (requiredType != null) {
            typeName = requiredType.getSimpleName();
        }

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid Argument Type",
                "Parameter '" + ex.getName() + "' should be of type " + typeName
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParams(MissingServletRequestParameterException ex) {
        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Missing Parameter",
                "Required parameter '" + ex.getParameterName() + "' is missing"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private Map<String, Object> createErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("path", "/api/users");
        return errorResponse;
    }
}
