package ru.aston.homework.intensive.userservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.aston.homework.intensive.userservice.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Testcontainers
class UserRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Очищаем базу данных перед каждым тестом
        userRepository.deleteAll();
    }

    @Test
    void testSaveUser() {
        User user = new User("John", "john@mail.ru", 30);
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());
        assertEquals("John", savedUser.getName());
        assertEquals("john@mail.ru", savedUser.getEmail());
        assertEquals(30, savedUser.getAge());
        assertNotNull(savedUser.getCreatedAt());
    }

    @Test
    void testFindById() {
        User user = new User("Jane", "jane@mail.ru", 25);
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("Jane", foundUser.get().getName());
        assertEquals("jane@mail.ru", foundUser.get().getEmail());
    }

    @Test
    void testFindAll() {
        User user1 = new User("User1", "user1@mail.ru", 20);
        User user2 = new User("User2", "user2@mail.ru", 30);
        user1.setCreatedAt(LocalDateTime.now());
        user2.setCreatedAt(LocalDateTime.now());
        userRepository.save(user1);
        userRepository.save(user2);
        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("User1")));
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("User2")));
    }

    @Test
    void testExistsByEmail() {
        User user = new User("Test", "test@mail.ru", 25);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        boolean exists = userRepository.existsByEmail("test@mail.ru");
        boolean notExists = userRepository.existsByEmail("nonexistent@mail.ru");
        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    void testExistsByEmailAndIdNot() {
        User user1 = new User("User1", "user1@mail.ru", 25);
        User user2 = new User("User2", "user2@mail.ru", 30);
        user1.setCreatedAt(LocalDateTime.now());
        user2.setCreatedAt(LocalDateTime.now());
        User savedUser1 = userRepository.save(user1);
        userRepository.save(user2);
        boolean exists = userRepository.existsByEmailAndIdNot("user2@mail.ru", savedUser1.getId());
        boolean notExists = userRepository.existsByEmailAndIdNot("user1@mail.ru", savedUser1.getId());
        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    void testDeleteById() {
        User user = new User("Delete", "delete@mail.ru", 40);
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        userRepository.deleteById(savedUser.getId());
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void testUpdateUser() {
        User user = new User("Original Name", "original@mail.ru", 25);
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        savedUser.setName("Updated Name");
        savedUser.setEmail("updated@mail.ru");
        savedUser.setAge(30);
        User updatedUser = userRepository.save(savedUser);
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("updated@mail.ru", updatedUser.getEmail());
        assertEquals(30, updatedUser.getAge());
        assertEquals(savedUser.getId(), updatedUser.getId());
    }

    @Test
    void testFindByEmail() {
        User user = new User("User", "user@mail.ru", 35);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        Optional<User> foundUser = userRepository.findByEmail("user@mail.ru");
        assertTrue(foundUser.isPresent());
        assertEquals("User", foundUser.get().getName());
    }

    @Test
    void testCountUsers() {
        User user1 = new User("User1", "user1@mail.ru", 20);
        User user2 = new User("User2", "user2@mail.ru", 30);
        userRepository.save(user1);
        userRepository.save(user2);
        long count = userRepository.count();
        assertEquals(2, count);
    }
}
