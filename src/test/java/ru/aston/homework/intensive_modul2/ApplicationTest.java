package ru.aston.homework.intensive_modul2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.aston.homework.intensive_modul2.service.UserService;
import ru.aston.homework.intensive_modul2.view.UserChoice;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationTest {

    @Mock
    private UserService userService;

    @Mock
    private Scanner scanner;

    @InjectMocks
    private Application application;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        application = new Application(userService);
    }

    @Test
    void testInitializeStrategies() {
        application.initializeStrategies();
        assertTrue(application.choiceToStrategy.containsKey(UserChoice.CREATE_USER));
        assertTrue(application.choiceToStrategy.containsKey(UserChoice.GET_USER_BY_ID));
        assertTrue(application.choiceToStrategy.containsKey(UserChoice.UPDATE_USER));
        assertTrue(application.choiceToStrategy.containsKey(UserChoice.DELETE_USER));
        assertTrue(application.choiceToStrategy.containsKey(UserChoice.LIST_ALL_USERS));
    }

    @Test
    void testGetUserChoiceValidInput() {
        when(scanner.nextLine()).thenReturn("1");
        int choice = application.getUserChoice(scanner);
        assertEquals(1, choice);
        verify(scanner).nextLine();
    }

    @Test
    void testGetUserChoiceEmptyInput() {
        when(scanner.nextLine()).thenReturn("");
        assertThrows(IllegalArgumentException.class, () -> application.getUserChoice(scanner));
        verify(scanner).nextLine();
    }

    @Test
    void testGetUserChoiceNonNumericInput() {
        when(scanner.nextLine()).thenReturn("abc");
        assertThrows(NumberFormatException.class, () -> application.getUserChoice(scanner));
        verify(scanner).nextLine();
    }

    @Test
    void testStrategiesMapping() {
        application.initializeStrategies();
        assertEquals(5, application.choiceToStrategy.size());
        assertTrue(application.choiceToStrategy.containsKey(UserChoice.CREATE_USER));
        assertTrue(application.choiceToStrategy.containsKey(UserChoice.GET_USER_BY_ID));
        assertTrue(application.choiceToStrategy.containsKey(UserChoice.UPDATE_USER));
        assertTrue(application.choiceToStrategy.containsKey(UserChoice.DELETE_USER));
        assertTrue(application.choiceToStrategy.containsKey(UserChoice.LIST_ALL_USERS));
    }
}
