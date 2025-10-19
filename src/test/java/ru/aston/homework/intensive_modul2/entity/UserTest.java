package ru.aston.homework.intensive_modul2.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
    }

    @Test
    void testSetAndGetId() {
        Long expectedId = 1L;
        user.setId(expectedId);
        assertEquals(expectedId, user.getId());
    }

    @Test
    void testSetAndGetName() {
        String expectedName = "Иван Иванов";
        user.setName(expectedName);
        assertEquals(expectedName, user.getName());
    }

    @Test
    void testSetAndGetEmail() {
        String expectedEmail = "test@example.com";
        user.setEmail(expectedEmail);
        assertEquals(expectedEmail, user.getEmail());
    }

    @Test
    void testSetAndGetAge() {
        int expectedAge = 25;
        user.setAge(expectedAge);
        assertEquals(expectedAge, user.getAge());
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime expectedDate = LocalDateTime.of(2025, 10, 17, 18, 26);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = expectedDate.format(formatter);
        user.setCreatedAt(expectedDate);
        assertEquals(formattedDate, user.getCreatedAt());
    }

    @Test
    void testCreatedAtFormat() {
        LocalDateTime testDate = LocalDateTime.of(2025, 10, 17, 18, 26);
        String expectedFormat = "2025-10-17 18:26:00";
        user.setCreatedAt(testDate);
        assertEquals(expectedFormat, user.getCreatedAt());
    }

    @Test
    void testNullValues() {
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertEquals(0, user.getAge());
    }
}
