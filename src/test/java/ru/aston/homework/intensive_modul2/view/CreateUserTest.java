package ru.aston.homework.intensive_modul2.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.service.UserService;
import java.util.Scanner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateUserTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private CreateUser createUser;

    private Scanner scanner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scanner = mock(Scanner.class);
    }

    @AfterEach
    void tearDown() {
        reset(userService, scanner);
    }

    @Test
    void testSuccessfulUserCreation() {
        when(scanner.nextLine()).thenReturn("John Doe")
                .thenReturn("john@example.com")
                .thenReturn("30");
        when(userService.create(any(User.class))).thenReturn(1L);
        createUser.invoke(scanner);
        verify(userService).create(any(User.class));
    }

    @Test
    void testInvalidEmail() {
        when(scanner.nextLine()).thenReturn("John Doe")
                .thenReturn("invalid-email")
                .thenReturn("john@example.com")
                .thenReturn("30");
        when(userService.create(any(User.class))).thenReturn(2L);
        createUser.invoke(scanner);
        verify(userService).create(any(User.class));
    }

    @Test
    void testEmptyName() {
        when(scanner.nextLine()).thenReturn("")
                .thenReturn("John Doe")
                .thenReturn("john@example.com")
                .thenReturn("30");
        when(userService.create(any(User.class))).thenReturn(4L);
        createUser.invoke(scanner);
        verify(userService).create(any(User.class));
    }

    @Test
    void testFailedUserCreation() {
        when(scanner.nextLine()).thenReturn("John Doe")
                .thenReturn("john@example.com")
                .thenReturn("30");
        when(userService.create(any(User.class))).thenReturn(-1L);
        createUser.invoke(scanner);
        verify(userService).create(any(User.class));
    }

    @Test
    void testEmptyEmail() {
        when(scanner.nextLine()).thenReturn("John Doe")
                .thenReturn("")
                .thenReturn("john@example.com")
                .thenReturn("30");
        when(userService.create(any(User.class))).thenReturn(5L);
        createUser.invoke(scanner);
        verify(userService).create(any(User.class));
    }

    @Test
    void testEmptyAge() {
        when(scanner.nextLine()).thenReturn("John Doe")
                .thenReturn("john@example.com")
                .thenReturn("")
                .thenReturn("30");
        when(userService.create(any(User.class))).thenReturn(6L);
        createUser.invoke(scanner);
        verify(userService).create(any(User.class));
    }

    @Test
    void testInvalidName() {
        when(scanner.nextLine()).thenReturn("John!Doe")
                .thenReturn("John Doe")
                .thenReturn("john@example.com")
                .thenReturn("30");
        when(userService.create(any(User.class))).thenReturn(7L);
        createUser.invoke(scanner);
        verify(userService).create(any(User.class));
    }

    @Test
    void testValidEdgeCases() {
        when(scanner.nextLine()).thenReturn("John Doe")
                .thenReturn("john@example.com")
                .thenReturn("1");
        when(userService.create(any(User.class))).thenReturn(8L);
        createUser.invoke(scanner);
        verify(userService).create(any(User.class));

        when(scanner.nextLine()).thenReturn("John Doe")
                .thenReturn("john@example.com")
                .thenReturn("120");
        when(userService.create(any(User.class))).thenReturn(9L);
        createUser.invoke(scanner);
        verify(userService, times(2)).create(any(User.class));
    }
}
