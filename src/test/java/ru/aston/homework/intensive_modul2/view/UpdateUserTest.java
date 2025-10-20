package ru.aston.homework.intensive_modul2.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.service.UserService;
import java.util.Optional;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateUserTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUserTest.class);

    @Mock
    private UserService userService;

    @Mock
    private Scanner scanner;

    @InjectMocks
    private UpdateUser updateUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInvokeSuccessfulUpdate() {
        Long userId = 1L;
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setAge(30);

        when(scanner.nextLong()).thenReturn(userId);
        when(scanner.nextLine()).thenReturn("Jane Doe", "jane@example.com", "35");
        try (MockedStatic<InputValidator> mockedStatic = mockStatic(InputValidator.class)) {
            mockedStatic.when(() -> InputValidator.validateName(any(), any(), anyBoolean()))
                    .thenReturn("Jane Doe");
            mockedStatic.when(() -> InputValidator.validateEmail(any(), any(), anyBoolean()))
                    .thenReturn("jane@example.com");
            mockedStatic.when(() -> InputValidator.validateAge(any(), any(), anyBoolean()))
                    .thenReturn(35);
            when(userService.findById(userId))
                    .thenReturn(Optional.of(user));
            updateUser = new UpdateUser(userService);
            updateUser.invoke(scanner);
            verify(userService, times(2)).findById(userId);
            verify(userService).update(user);
            assertEquals("Jane Doe", user.getName());
            assertEquals("jane@example.com", user.getEmail());
            assertEquals(35, user.getAge());
        }
    }


    @Test
    void testInvokeUserNotFound() {
        Long userId = 2L;
        when(scanner.nextLong()).thenReturn(userId);
        when(userService.findById(userId)).thenReturn(Optional.empty());
        updateUser.invoke(scanner);
        verify(userService).findById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testInvokeInvalidIdFormat() {
        when(scanner.nextLong()).thenThrow(NumberFormatException.class);
        updateUser.invoke(scanner);
        verifyNoInteractions(userService);
    }

    @Test
    void testUpdateNameBlankInput() {
        User user = new User();
        user.setName("John Doe");
        when(scanner.nextLine()).thenReturn("");
        updateUser.updateName(user, scanner);
        assertEquals("John Doe", user.getName());
    }

    @Test
    void testUpdateNameInvalidName() {

        User user = new User();
        user.setName("John Doe");
        when(scanner.nextLine()).thenReturn("John!Doe", "John Doe");
        updateUser.updateName(user, scanner);
        assertEquals("John Doe", user.getName());
    }

    @Test
    void testUpdateEmailBlankInput() {
        User user = new User();
        user.setEmail("john@example.com");
        when(scanner.nextLine()).thenReturn("");
        updateUser.updateEmail(user, scanner);
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void testUpdateEmailInvalidEmail() {
        User user = new User();
        user.setEmail("john@example.com");
        when(scanner.nextLine()).thenReturn("invalid-email", "john.smith@example.com");
        updateUser.updateEmail(user, scanner);
        assertEquals("john.smith@example.com", user.getEmail());
    }

    @Test
    void testUpdateAgeBlankInput() {
        User user = new User();
        user.setAge(30);
        when(scanner.nextLine()).thenReturn("");
        updateUser.updateAge(user, scanner);
        assertEquals(30, user.getAge());
    }

    @Test
    void testUpdateAgeInvalidAge() {

        User user = new User();
        user.setAge(30);
        when(scanner.nextLine()).thenReturn("-5", "130", "35");
        updateUser.updateAge(user, scanner);
        assertEquals(35, user.getAge());
    }

    @Test
    void testVerifyUpdateUserFound() {
        Long userId = 3L;
        User updatedUser = new User();
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setAge(40);
        when(userService.findById(userId)).thenReturn(Optional.of(updatedUser));
        updateUser.verifyUpdate(userId);
        verify(userService).findById(userId);
    }

    @Test
    void testVerifyUpdateUserNotFound() {
        Long userId = 4L;
        when(userService.findById(userId)).thenReturn(Optional.empty());
        updateUser.verifyUpdate(userId);
        verify(userService).findById(userId);
    }

    @Test
    void testUpdateAgeNonNumericInput() {
        User user = new User();
        user.setAge(30);
        when(scanner.nextLine()).thenReturn("abc", "35");
        updateUser.updateAge(user, scanner);
        assertEquals(35, user.getAge());
    }

    @Test
    void testLoggingUserNotFound() {
        Long userId = 2L;
        when(scanner.nextLong()).thenReturn(userId);
        when(userService.findById(userId)).thenReturn(Optional.empty());
        updateUser.invoke(scanner);
        LOGGER.info("User not found with ID: {}", userId);
        verify(userService).findById(userId);
        assertThat(userService.findById(userId)).isNotPresent();
    }

    @Test
    void testDisplayCurrentUserData() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAge(25);
        updateUser.displayCurrentUserData(user);
        assertThat(user.getName()).isEqualTo("Test User");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getAge()).isEqualTo(25);
    }

    @Test
    void testUpdateAllFields() {
        User user = new User();
        user.setName("Old Name");
        user.setEmail("old@example.com");
        user.setAge(20);
        when(scanner.nextLine()).thenReturn(
                "New Name",
                "new@example.com",
                "25"
        );
        updateUser.updateUserFields(user, scanner);
        assertEquals("New Name", user.getName());
        assertEquals("new@example.com", user.getEmail());
        assertEquals(25, user.getAge());
    }

    @Test
    void testUpdateNameWithSpaces() {
        User user = new User();
        user.setName("John Doe");
        when(scanner.nextLine()).thenReturn("  John Doe  ");
        updateUser.updateName(user, scanner);
        assertEquals("John Doe", user.getName());
    }

    @Test
    void testUpdateEmailValidDomains() {
        User user = new User();
        user.setEmail("john@example.com");
        when(scanner.nextLine()).thenReturn( "test@domain.co.uk");
        updateUser.updateEmail(user, scanner);
        assertEquals("test@domain.co.uk", user.getEmail());
    }
}
