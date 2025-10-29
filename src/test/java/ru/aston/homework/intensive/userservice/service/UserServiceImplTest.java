package ru.aston.homework.intensive.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.homework.intensive.userservice.controller.dto.CreateUserDto;
import ru.aston.homework.intensive.userservice.controller.dto.UpdateUserDto;
import ru.aston.homework.intensive.userservice.controller.dto.UserResponseDto;
import ru.aston.homework.intensive.userservice.entity.User;
import ru.aston.homework.intensive.userservice.exception.EmailAlreadyExistsException;
import ru.aston.homework.intensive.userservice.exception.UserNotFoundException;
import ru.aston.homework.intensive.userservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        testUser = new User();
        testUser.setName("Test");
        testUser.setEmail("test@mail.ru");
        testUser.setAge(25);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser = userRepository.save(testUser);
    }

    @Test
    void testGetUserByIdShouldReturnUserWhenUserExists() {
        UserResponseDto result = userService.getUserById(testUser.getId());
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getAge(), result.getAge());
    }

    @Test
    void testGetUserByIdShouldThrowWhenUserNotExists() {
        assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(999L));
    }

    @Test
    void testGetAllUsersShouldReturnListOfUsers() {
        User user2 = new User("User2", "user2@mail.ru", 30);
        user2.setCreatedAt(LocalDateTime.now());
        userRepository.save(user2);
        List<UserResponseDto> result = userService.getAllUsers();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllUsersShouldReturnEmptyListWhenNoUsers() {
        userRepository.deleteAll();
        List<UserResponseDto> result = userService.getAllUsers();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateUserShouldCreateUserWhenValidData() {
        CreateUserDto createDto = new CreateUserDto("New", "new@mail.ru", 30);
        UserResponseDto result = userService.createUser(createDto);
        assertNotNull(result.getId());
        assertEquals("New", result.getName());
        assertEquals("new@mail.ru", result.getEmail());
        assertEquals(30, result.getAge());
        assertNotNull(result.getCreatedAt());
        assertTrue(userRepository.existsByEmail("new@mail.ru"));
    }

    @Test
    void testCreateUserShouldThrowWhenEmailAlreadyExists() {
        CreateUserDto createDto = new CreateUserDto("Another User", "test@mail.ru", 30);
        assertThrows(EmailAlreadyExistsException.class,
                () -> userService.createUser(createDto));
    }

    @Test
    void testUpdateUserShouldUpdateUserWhenValidData() {
        UpdateUserDto updateDto = new UpdateUserDto("Updated User", "updated@mail.ru", 35);
        UserResponseDto result = userService.updateUser(testUser.getId(), updateDto);
        assertNotNull(result);
        assertEquals("Updated User", result.getName());
        assertEquals("updated@mail.ru", result.getEmail());
        assertEquals(35, result.getAge());
        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertEquals("Updated User", updatedUser.getName());
        assertEquals("updated@mail.ru", updatedUser.getEmail());
    }

    @Test
    void testUpdateUserShouldUpdatePartialData() {
        UpdateUserDto updateDto = new UpdateUserDto();
        updateDto.setName("Updated Name Only");
        UserResponseDto result = userService.updateUser(testUser.getId(), updateDto);
        assertEquals("Updated Name Only", result.getName());
        assertEquals("test@mail.ru", result.getEmail());
        assertEquals(25, result.getAge());
    }

    @Test
    void testUpdateUserShouldThrowWhenUserNotFound() {
        UpdateUserDto updateDto = new UpdateUserDto("Updated User", "updated@mail.ru", 35);
        assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(999L, updateDto));
    }

    @Test
    void testDeleteUserShouldDeleteUserWhenUserExists() {
        userService.deleteUser(testUser.getId());
        assertFalse(userRepository.existsById(testUser.getId()));
    }

    @Test
    void testDeleteUserShouldThrowWhenUserNotFound() {
        assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(999L));
    }

    @Test
    void testExistsByIdShouldReturnTrueWhenUserExists() {
        boolean result = userService.existsById(testUser.getId());
        assertTrue(result);
    }

    @Test
    void testExistsByIdShouldReturnFalseWhenUserNotExists() {
        boolean result = userService.existsById(999L);
        assertFalse(result);
    }

    @Test
    void testExistsByEmailShouldReturnTrueWhenEmailExists() {
        boolean result = userService.existsByEmail("test@mail.ru");
        assertTrue(result);
    }

    @Test
    void testExistsByEmailShouldReturnFalseWhenEmailNotExists() {
        boolean result = userService.existsByEmail("nonexistent@mail.ru");
        assertFalse(result);
    }
}
