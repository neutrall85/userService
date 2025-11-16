package ru.aston.homework.intensive.notificationservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.homework.intensive.circuitbreaker.service.CircuitBreakerService;
import ru.aston.homework.intensive.notificationservice.dto.EmailRequest;
import ru.aston.homework.intensive.notificationservice.service.EmailService;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EmailService emailService;
    private final CircuitBreakerService circuitBreakerService;

    public NotificationController(EmailService emailService, CircuitBreakerService circuitBreakerService) {
        this.emailService = emailService;
        this.circuitBreakerService = circuitBreakerService;
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendEmail(
                    emailRequest.getEmail(),
                    emailRequest.getSubject(),
                    emailRequest.getMessage()
            );

            return ResponseEntity.ok("Email sent successfully to: " + emailRequest.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }

    @PostMapping("/welcome")
    public ResponseEntity<String> sendWelcomeEmail(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            Long mockUserId = System.currentTimeMillis();
            emailService.sendUserCreationNotification(emailRequest.getEmail(), mockUserId);

            return ResponseEntity.ok("Welcome email sent successfully to: " + emailRequest.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send welcome email: " + e.getMessage());
        }
    }

    @PostMapping("/account-deleted")
    public ResponseEntity<String> sendAccountDeletedEmail(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            Long mockUserId = System.currentTimeMillis();
            emailService.sendUserDeletionNotification(emailRequest.getEmail(), mockUserId);

            return ResponseEntity.ok("Account deletion notification sent successfully to: " + emailRequest.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send account deletion notification: " + e.getMessage());
        }
    }

    @GetMapping("/circuit-breakers")
    public ResponseEntity<Map<String, Map<String, Object>>> getCircuitBreakers() {
        return ResponseEntity.ok(circuitBreakerService.getAllCircuitBreakers());
    }

    @GetMapping("/circuit-breakers/{name}")
    public ResponseEntity<Map<String, Object>> getCircuitBreaker(@PathVariable String name) {
        Map<String, Object> state = circuitBreakerService.getCircuitBreakerState(name);
        if (state.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(state);
        }
        return ResponseEntity.ok(state);
    }

    @PostMapping("/circuit-breakers/{name}/reset")
    public ResponseEntity<Map<String, String>> resetCircuitBreaker(@PathVariable String name) {
        try {
            circuitBreakerService.resetCircuitBreaker(name);
            return ResponseEntity.ok(Map.of(
                    "circuitBreaker", name,
                    "action", "reset",
                    "status", "success"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "circuitBreaker", name,
                    "action", "reset",
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/health")
    public String health() {
        return "Notification service is healthy";
    }
}
