package ru.aston.homework.intensive.userservice.controller.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserEventTest {

    @Test
    void testNoArgsConstructor() {
        UserEvent userEvent = new UserEvent();
        assertNotNull(userEvent);
        assertNull(userEvent.getOperation());
        assertNull(userEvent.getEmail());
        assertNull(userEvent.getTimestamp());
        assertNull(userEvent.getUserId());
    }

    @Test
    void testAllArgsConstructor() {
        String operation = "CREATE";
        String email = "test@mail.ru";
        Long userId = 123L;
        UserEvent userEvent = new UserEvent(operation, email, userId);
        assertNotNull(userEvent);
        assertEquals(operation, userEvent.getOperation());
        assertEquals(email, userEvent.getEmail());
        assertEquals(userId, userEvent.getUserId());
        assertNotNull(userEvent.getTimestamp());
        LocalDateTime now = LocalDateTime.now();
        assertTrue(userEvent.getTimestamp().isBefore(now.plusSeconds(1)));
        assertTrue(userEvent.getTimestamp().isAfter(now.minusSeconds(1)));
    }

    @Test
    void testSettersAndGetters() {

        UserEvent userEvent = new UserEvent();
        String operation = "UPDATE";
        String email = "update@mail.ru";
        LocalDateTime timestamp = LocalDateTime.of(2023, 1, 1, 12, 0);
        Long userId = 456L;
        userEvent.setOperation(operation);
        userEvent.setEmail(email);
        userEvent.setTimestamp(timestamp);
        userEvent.setUserId(userId);
        assertEquals(operation, userEvent.getOperation());
        assertEquals(email, userEvent.getEmail());
        assertEquals(timestamp, userEvent.getTimestamp());
        assertEquals(userId, userEvent.getUserId());
    }

    @Test
    void testToString() {
        UserEvent userEvent = new UserEvent("DELETE", "delete@mail.ru", 789L);
        String toStringResult = userEvent.toString();
        assertThat(toStringResult)
            .contains("UserEvent")
            .contains("operation='DELETE'")
            .contains("email='delete@mail.ru'")
            .contains("userId=789")
            .contains("timestamp=");
    }

    @Test
    void testTimestampAutoGeneration() {
        LocalDateTime beforeCreation = LocalDateTime.now();
        UserEvent userEvent = new UserEvent("CREATE", "test@mail.ru", 111L);
        LocalDateTime afterCreation = LocalDateTime.now();
        assertThat(userEvent.getTimestamp())
            .isAfter(beforeCreation)
            .isBefore(afterCreation);
    }

    @Test
    void testWithDifferentOperations() {
        String[] operations = {"CREATE", "UPDATE", "DELETE", "READ"};
        for (String operation : operations) {
            UserEvent userEvent = new UserEvent(operation, "user@mail.ru", 999L);
            assertEquals(operation, userEvent.getOperation());
            assertEquals("user@mail.ru", userEvent.getEmail());
            assertEquals(999L, userEvent.getUserId());
            assertNotNull(userEvent.getTimestamp());
        }
    }

    @Test
    void testWithNullValues() {
        UserEvent userEvent = new UserEvent(null, null, null);
        assertNull(userEvent.getOperation());
        assertNull(userEvent.getEmail());
        assertNull(userEvent.getUserId());
        assertNotNull(userEvent.getTimestamp());
    }

    @Test
    void testContextLoads() {
        assertTrue(true, "Context should load successfully");
    }
}
