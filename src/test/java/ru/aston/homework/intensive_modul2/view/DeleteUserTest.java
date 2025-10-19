package ru.aston.homework.intensive_modul2.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.homework.intensive_modul2.service.UserService;
import java.util.Scanner;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserTest.class);

    @Mock
    private UserService userService;

    @InjectMocks
    private DeleteUser deleteUser;

    @BeforeEach
    void setUp() {
        Mockito.reset(userService);
    }

    @Test
    void testSuccessfulDeletion() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLong()).thenReturn(1L);
        when(scanner.nextLine()).thenReturn("");
        when(userService.exists(1L)).thenReturn(true);
        deleteUser.invoke(scanner);
        verify(userService).exists(1L);
        verify(userService).delete(1L);
        LOGGER.info("Successful deletion test passed");
    }

    @Test
    void testUserNotFound() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLong()).thenReturn(2L);
        when(scanner.nextLine()).thenReturn("");
        when(userService.exists(2L)).thenReturn(false);
        deleteUser.invoke(scanner);
        verify(userService).exists(2L);
        verify(userService, never()).delete(anyLong());
        LOGGER.info("User not found test passed");
    }

    @Test
    void testNegativeId() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLong()).thenReturn(-1L);
        when(scanner.nextLine()).thenReturn("");
        when(userService.exists(-1L)).thenReturn(false);
        deleteUser.invoke(scanner);
        verify(userService).exists(-1L);
        verify(userService, never()).delete(anyLong());
        LOGGER.info("Negative ID test passed");
    }

    @Test
    void testZeroId() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLong()).thenReturn(0L);
        when(scanner.nextLine()).thenReturn("");
        when(userService.exists(0L)).thenReturn(false);
        deleteUser.invoke(scanner);
        verify(userService).exists(0L);
        verify(userService, never()).delete(anyLong());
        LOGGER.info("Zero ID test passed");
    }

    @Test
    void testGeneralException() {
        Scanner scanner = mock(Scanner.class);
        when(scanner.nextLong()).thenReturn(3L);
        when(scanner.nextLine()).thenReturn("");
        when(userService.exists(3L)).thenReturn(true);
        deleteUser.invoke(scanner);
        verify(userService).exists(3L);
        verify(userService).delete(3L);
        LOGGER.info("General exception test passed");
    }
}
