package ru.aston.homework.intensive.notificationservice.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@SpringBootTest
@Testcontainers
class UserEventTest {

    @Test
    void shouldCreateUserEventWithConstructor() {
        String operation = "CREATE";
        String email = "user@mail.ru";
        Long userId = 123L;
        UserEvent userEvent = new UserEvent(operation, email, userId);
        assertThat(userEvent.getOperation()).isEqualTo(operation);
        assertThat(userEvent.getEmail()).isEqualTo(email);
        assertThat(userEvent.getUserId()).isEqualTo(userId);
        assertThat(userEvent.getTimestamp()).isNotNull();
        assertThat(userEvent.getTimestamp()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
    }

    @Test
    void shouldCreateUserEventWithDefaultConstructor() {
        UserEvent userEvent = new UserEvent();
        assertThat(userEvent.getOperation()).isNull();
        assertThat(userEvent.getEmail()).isNull();
        assertThat(userEvent.getUserId()).isNull();
        assertThat(userEvent.getTimestamp()).isNull();
    }

    @Test
    void shouldSetAndGetProperties() {
        UserEvent userEvent = new UserEvent();
        String operation = "UPDATE";
        String email = "update@mail.ru";
        Long userId = 456L;
        LocalDateTime timestamp = LocalDateTime.now();
        userEvent.setOperation(operation);
        userEvent.setEmail(email);
        userEvent.setUserId(userId);
        userEvent.setTimestamp(timestamp);
        assertThat(userEvent.getOperation()).isEqualTo(operation);
        assertThat(userEvent.getEmail()).isEqualTo(email);
        assertThat(userEvent.getUserId()).isEqualTo(userId);
        assertThat(userEvent.getTimestamp()).isEqualTo(timestamp);
    }

    @Test
    void shouldHandleNullValues() {
        UserEvent userEvent = new UserEvent("OPERATION", "email@mail.ru", 1L);
        userEvent.setOperation(null);
        userEvent.setEmail(null);
        userEvent.setUserId(null);
        userEvent.setTimestamp(null);
        assertThat(userEvent.getOperation()).isNull();
        assertThat(userEvent.getEmail()).isNull();
        assertThat(userEvent.getUserId()).isNull();
        assertThat(userEvent.getTimestamp()).isNull();
    }

    @Test
    void shouldGenerateCorrectToString() {
        UserEvent userEvent = new UserEvent("DELETE", "delete@mail.ru", 789L);
        LocalDateTime fixedTimestamp = LocalDateTime.of(2023, 12, 1, 10, 30, 0);
        userEvent.setTimestamp(fixedTimestamp);
        String toStringResult = userEvent.toString();
        assertThat(toStringResult).contains("operation='DELETE'")
                .contains("email='delete@mail.ru'")
                .contains("userId=789")
                .contains("timestamp=2023-12-01T10:30");
    }

    @Test
    void shouldHandleSpecialCharactersInToString() {
        UserEvent userEvent = new UserEvent("SPECIAL_OP", "special@gmail.com", 999L);
        userEvent.setOperation("Operation with 'quotes'");
        userEvent.setEmail("email with spaces@gmail.com");
        String toStringResult = userEvent.toString();
        assertThat(toStringResult).contains("operation='Operation with 'quotes''").contains("email='email with spaces@gmail.com'");
    }

    @Test
    void shouldCreateMultipleEventsWithDifferentTimestamps() {
        UserEvent event1 = new UserEvent("OP1", "test1@mail.ru", 1L);
        UserEvent event2 = new UserEvent("OP2", "test2@mail.ru", 2L);
        assertThat(event1.getTimestamp()).isBefore(event2.getTimestamp());
    }

    @Test
    void shouldHandleLargeUserId() {
        Long largeUserId = Long.MAX_VALUE;
        UserEvent userEvent = new UserEvent("TEST", "test@mail.ru", largeUserId);
        assertThat(userEvent.getUserId()).isEqualTo(largeUserId);
    }

    @Test
    void shouldHandleZeroUserId() {
        Long zeroUserId = 0L;
        UserEvent userEvent = new UserEvent("TEST", "test@mail.ru", zeroUserId);
        assertThat(userEvent.getUserId()).isEqualTo(zeroUserId);
    }
}
