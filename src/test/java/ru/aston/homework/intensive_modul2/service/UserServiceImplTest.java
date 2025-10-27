package ru.aston.homework.intensive_modul2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.homework.intensive_modul2.controller.dto.CreateUserDto;
import ru.aston.homework.intensive_modul2.controller.dto.UpdateUserDto;
import ru.aston.homework.intensive_modul2.controller.dto.UserResponseDto;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private CreateUserDto createUserDto;
    private UpdateUserDto updateUserDto;
    private final Long userId = 1L;
    private final String userEmail = "test@example.com";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");
        testUser.setEmail(userEmail);
        testUser.setAge(25);
        testUser.setCreatedAt(LocalDateTime.now());

        createUserDto = new CreateUserDto();
        createUserDto.setName("New User");
        createUserDto.setEmail("new@example.com");
        createUserDto.setAge(30);

        updateUserDto = new UpdateUserDto();
        updateUserDto.setName("Updated User");
        updateUserDto.setEmail("updated@example.com");
        updateUserDto.setAge(35);
    }

    @Test
    void testGetUserByIdShouldReturnUserWhenUserExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        UserResponseDto result = userService.getUserById(userId);
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getAge(), result.getAge());
        assertEquals(testUser.getCreatedAt(), result.getCreatedAt());
        verify(userRepository).findById(userId);
    }

    @Test
    void testGetAllUsersShouldReturnListOfUsers() {
        User user2 = new User();
        user2.setId(2L);
        user2.setName("User 2");
        user2.setEmail("user2@example.com");
        user2.setAge(28);
        user2.setCreatedAt(LocalDateTime.now());
        List<User> users = List.of(testUser, user2);
        when(userRepository.findAll()).thenReturn(users);
        List<UserResponseDto> result = userService.getAllUsers();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testUser.getId(), result.get(0).getId());
        assertEquals(user2.getId(), result.get(1).getId());

        verify(userRepository).findAll();
    }

    @Test
    void testGetAllUsersShouldReturnEmptyListWhenNoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());
        List<UserResponseDto> result = userService.getAllUsers();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    void testCreateUserShouldCreateUserWhenValidData() {
        when(userRepository.existsByEmail(createUserDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        UserResponseDto result = userService.createUser(createUserDto);
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getName(), result.getName());
        assertEquals(testUser.getEmail(), result.getEmail());
        verify(userRepository).existsByEmail(createUserDto.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUserShouldThrowRuntimeExceptionWhenUnexpectedError() {
        when(userRepository.existsByEmail(createUserDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.createUser(createUserDto));
        assertEquals("Failed to create user", exception.getMessage());
        verify(userRepository).existsByEmail(createUserDto.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUserShouldThrowIllegalArgumentExceptionWhenAgeIsNull() {
        createUserDto.setAge(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(createUserDto));
        assertEquals("Age cannot be null", exception.getMessage());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUserShouldUpdateUserWhenValidData() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmailAndIdNot(updateUserDto.getEmail(), userId)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        UserResponseDto result = userService.updateUser(userId, updateUserDto);
        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmailAndIdNot(updateUserDto.getEmail(), userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUserShouldUpdateOnlyNameWhenOnlyNameProvided() {
        UpdateUserDto partialUpdate = new UpdateUserDto();
        partialUpdate.setName("Updated Name");
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        UserResponseDto result = userService.updateUser(userId, partialUpdate);
        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userRepository, never()).existsByEmailAndIdNot(anyString(), anyLong());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUserShouldUpdateOnlyEmailWhenOnlyEmailProvided() {
        UpdateUserDto partialUpdate = new UpdateUserDto();
        partialUpdate.setEmail("newemail@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmailAndIdNot(partialUpdate.getEmail(), userId)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        UserResponseDto result = userService.updateUser(userId, partialUpdate);
        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmailAndIdNot(partialUpdate.getEmail(), userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUserShouldUpdateOnlyAgeWhenOnlyAgeProvided() {
        UpdateUserDto partialUpdate = new UpdateUserDto();
        partialUpdate.setAge(40);
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        UserResponseDto result = userService.updateUser(userId, partialUpdate);
        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userRepository, never()).existsByEmailAndIdNot(anyString(), anyLong());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUserShouldHandleUnexpectedError() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmailAndIdNot(updateUserDto.getEmail(), userId)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(userId, updateUserDto));
        assertEquals("Failed to update user", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(userRepository).existsByEmailAndIdNot(updateUserDto.getEmail(), userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testDeleteUserShouldDeleteUserWhenUserExists() {
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);
        userService.deleteUser(userId);
        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testExistsByIdShouldReturnTrueWhenUserExists() {
        when(userRepository.existsById(userId)).thenReturn(true);
        boolean result = userService.existsById(userId);
        assertTrue(result);
        verify(userRepository).existsById(userId);
    }

    @Test
    void testExistsById_ShouldReturnFalse_WhenUserNotExists() {
        when(userRepository.existsById(userId)).thenReturn(false);
        boolean result = userService.existsById(userId);
        assertFalse(result);
        verify(userRepository).existsById(userId);
    }

    @Test
    void testExistsByEmailShouldReturnTrueWhenEmailExists() {
        when(userRepository.existsByEmail(userEmail)).thenReturn(true);
        boolean result = userService.existsByEmail(userEmail);
        assertTrue(result);
        verify(userRepository).existsByEmail(userEmail);
    }

    @Test
    void testExistsByEmailShouldReturnFalseWhenEmailNotExists() {
        when(userRepository.existsByEmail(userEmail)).thenReturn(false);
        boolean result = userService.existsByEmail(userEmail);
        assertFalse(result);
        verify(userRepository).existsByEmail(userEmail);
    }
}