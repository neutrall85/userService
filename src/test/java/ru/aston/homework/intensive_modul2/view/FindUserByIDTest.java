package ru.aston.homework.intensive_modul2.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.service.UserService;

import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByIDTest {
    @Mock
    private UserService userService;

    @Mock
    private Scanner scanner;

    @InjectMocks
    private FindUserByID findUserByID;

    @BeforeEach
    void setUp() {
        Mockito.reset(userService, scanner);
    }

    @Test
    void testInvoke_UserFound() {
        Long testId = 1L;
        User testUser = mock(User.class);

        when(testUser.getId()).thenReturn(testId);
        when(testUser.getName()).thenReturn("John Doe");
        when(testUser.getEmail()).thenReturn("john@example.com");
        when(testUser.getAge()).thenReturn(30);
        when(testUser.getCreatedAt()).thenReturn(null);

        when(scanner.nextLong()).thenReturn(testId);
        when(userService.findById(testId)).thenReturn(Optional.of(testUser));

        findUserByID.invoke(scanner);

        verify(scanner).nextLong();
        verify(userService).findById(testId);
        verify(scanner).nextLine();
    }

    @Test
    void testInvoke_UserNotFound() {
        Long testId = 2L;

        when(scanner.nextLong()).thenReturn(testId);
        when(userService.findById(testId)).thenReturn(Optional.empty());

        findUserByID.invoke(scanner);

        verify(scanner).nextLong();
        verify(userService).findById(testId);
        verify(scanner).nextLine();
    }

    @Test
    void testInvoke_InvalidInput() {
        when(scanner.nextLong()).thenThrow(NumberFormatException.class);

        try {
            findUserByID.invoke(scanner);
        } catch (NumberFormatException e) {
            assertTrue(true);
        }

        verify(scanner).nextLong();
        verifyNoInteractions(userService);
    }

    @Test
    void testInvoke_EmptyInput() {
        when(scanner.nextLong()).thenReturn(0L);
        when(scanner.nextLine()).thenReturn("");
        when(userService.findById(0L)).thenReturn(Optional.empty());

        findUserByID.invoke(scanner);

        verify(scanner).nextLong();
        verify(scanner).nextLine();
        verify(userService).findById(0L);
    }
}
