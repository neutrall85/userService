package ru.aston.homework.intensive_modul2.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getAge());
        assertNull(user.getCreatedAt());
    }

    @Test
    void testParameterizedConstructor() {
        String name = "John Doe";
        String email = "john.doe@example.com";
        Integer age = 30;
        user = new User(name, email, age);
        assertNotNull(user);
        assertNull(user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(age, user.getAge());
        assertNull(user.getCreatedAt());
    }

    @Test
    void testSettersAndGetters() {
        Long id = 1L;
        String name = "Jane Smith";
        String email = "jane.smith@example.com";
        Integer age = 25;
        LocalDateTime createdAt = LocalDateTime.now();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        user.setCreatedAt(createdAt);
        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(age, user.getAge());
        assertEquals(createdAt, user.getCreatedAt());
    }

    @Test
    void testOnCreate() throws Exception {
        user = new User("Test User", "test@example.com", 35);
        user.getClass().getDeclaredMethod("onCreate").invoke(user);
        assertNotNull(user.getCreatedAt());
        assertTrue(user.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(user.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    @Test
    void testEmailUniquenessConstraint() {
        User user1 = new User("User1", "same@email.com", 20);
        User user2 = new User("User2", "same@email.com", 25);
        assertEquals("same@email.com", user1.getEmail());
        assertEquals("same@email.com", user2.getEmail());
    }

    @Test
    void testNullabilityConstraints() {
        user = new User();
        user.setId(null);
        user.setCreatedAt(null);
        assertNull(user.getId());
        assertNull(user.getCreatedAt());
    }

    @Test
    void testAgeValidation() {
        user.setAge(0);
        assertEquals(0, user.getAge());
        user.setAge(150);
        assertEquals(150, user.getAge());
        user.setAge(null);
        assertNull(user.getAge());
    }

    @Test
    void testNameAndEmailLength() {
        String longName = "A".repeat(255);
        String longEmail = "test@" + "a".repeat(250) + ".com";
        user.setName(longName);
        user.setEmail(longEmail);
        assertEquals(longName, user.getName());
        assertEquals(longEmail, user.getEmail());
    }

    @Test
    void testCreatedAtNotUpdatable() {
        LocalDateTime initialTime = LocalDateTime.now().minusDays(1);
        user.setCreatedAt(initialTime);
        LocalDateTime newTime = LocalDateTime.now();
        user.setCreatedAt(newTime);
        assertEquals(newTime, user.getCreatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User("John Doe", "john@example.com", 30);
        User user2 = new User("John Doe", "john@example.com", 30);
        assertNotEquals(user1, user2);
    }
}
