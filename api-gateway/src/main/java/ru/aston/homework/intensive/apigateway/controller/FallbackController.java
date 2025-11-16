package ru.aston.homework.intensive.apigateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FallbackController.class);

    @GetMapping("/user-service")
    public ResponseEntity<Map<String, Object>> userServiceFallback() {
        LOGGER.warn("User Service fallback triggered - service unavailable");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                "message", "User Service is temporarily unavailable",
                "status", "SERVICE_UNAVAILABLE",
                "timestamp", LocalDateTime.now(),
                "service", "user-service",
                "fallback", true,
                "circuitBreaker", "userService"
        ));
    }

    @GetMapping("/notification-service")
    public ResponseEntity<Map<String, Object>> notificationServiceFallback() {
        LOGGER.warn("Notification Service fallback triggered - service unavailable");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                "message", "Notification Service is temporarily unavailable",
                "status", "SERVICE_UNAVAILABLE",
                "timestamp", LocalDateTime.now(),
                "service", "notification-service",
                "fallback", true,
                "circuitBreaker", "notificationService"
        ));
    }

    @GetMapping("/health")
    public String health() {
        return "API Gateway Fallback Controller is healthy";
    }
}
