package ru.aston.homework.intensive.notificationservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.homework.intensive.notificationservice.dto.EmailRequest;
import ru.aston.homework.intensive.notificationservice.service.EmailService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
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

    @PostMapping("/health")
    public String health() {
        return "Notification service is healthy";
    }
}

