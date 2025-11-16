package ru.aston.homework.intensive.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.aston.homework.intensive.notificationservice.dto.UserEvent;
import ru.aston.homework.intensive.circuitbreaker.service.CircuitBreakerService;

@Service
public class UserEventsConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventsConsumer.class);

    private final EmailService emailService;
    private final CircuitBreakerService circuitBreakerService;

    public UserEventsConsumer(EmailService emailService, CircuitBreakerService circuitBreakerService) {
        this.emailService = emailService;
        this.circuitBreakerService = circuitBreakerService;
    }

    @KafkaListener(topics = "${app.kafka.topics.user-created:user-created-topic}")
    public void consumeUserCreatedEvent(UserEvent event) {
        LOGGER.info("Received USER CREATED event: {}", event);

        circuitBreakerService.executeWithCircuitBreaker("notificationService", () -> {
            try {
                emailService.sendUserCreationNotification(event.getEmail(), event.getUserId());
                LOGGER.info("Successfully processed user creation event for user: {}", event.getEmail());
            } catch (Exception e) {
                LOGGER.error("Failed to process user creation event for user: {}. Error: {}",
                        event.getEmail(), e.getMessage());
                throw e;
            }
            return null;
        });
    }

    @KafkaListener(topics = "${app.kafka.topics.user-deleted:user-deleted-topic}")
    public void consumeUserDeletedEvent(UserEvent event) {
        LOGGER.info("Received USER DELETED event: {}", event);

        circuitBreakerService.executeWithCircuitBreaker("notificationService", () -> {
            try {
                emailService.sendUserDeletionNotification(event.getEmail(), event.getUserId());
                LOGGER.info("Successfully processed user deletion event for user: {}", event.getEmail());
            } catch (Exception e) {
                LOGGER.error("Failed to process user deletion event for user: {}. Error: {}",
                        event.getEmail(), e.getMessage());
                throw e;
            }
            return null;
        });
    }
}
