package ru.aston.homework.intensive_modul2.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InputValidatorTest {

    @Mock
    private Scanner scanner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateEmailForUpdateValidEmail() {
        when(scanner.nextLine()).thenReturn("test@example.com");
        String result = InputValidator.validateEmailForUpdate(scanner, "old@email.com");
        assertEquals("test@example.com", result);
        verify(scanner, times(1)).nextLine();
    }

    @Test
    void testValidateEmailForUpdateMaxAttempts() {
        when(scanner.nextLine()).thenReturn("invalid1", "invalid2", "invalid3");
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateEmailForUpdate(scanner, "old@email.com"));
        verify(scanner, times(3)).nextLine();
    }


    @Test
    void testValidateAgeForUpdateValidAge() {
        when(scanner.nextLine()).thenReturn("30");
        Integer result = InputValidator.validateAgeForUpdate(scanner, 25);
        assertEquals(30, result);
        verify(scanner, times(1)).nextLine();
    }

    @Test
    void testValidateAgeForUpdateMaxAttempts() {
        when(scanner.nextLine()).thenReturn("abc", "abc", "abc");
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateAgeForUpdate(scanner, 25));
        verify(scanner, times(3)).nextLine();
    }




    @Test
    void testValidateNameForUpdateValidNameWithHyphen() {
        when(scanner.nextLine()).thenReturn("Ann");
        String result = InputValidator.validateNameForUpdate(scanner, "Old Name");
        assertEquals("Ann", result);
        verify(scanner, times(1)).nextLine();
    }

    @Test
    void testValidateNameForUpdateMixedLanguages() {
        when(scanner.nextLine()).thenReturn("John Smith");
        String result = InputValidator.validateNameForUpdate(scanner, "Old Name");
        assertEquals("John Smith", result);
        verify(scanner, times(1)).nextLine();
    }

    @Test
    void testValidateNameForUpdateSingleSpace() {
        when(scanner.nextLine()).thenReturn(" Ivan ");
        String result = InputValidator.validateNameForUpdate(scanner, "Old Name");
        assertEquals("Ivan", result);
        verify(scanner, times(1)).nextLine();
    }
}