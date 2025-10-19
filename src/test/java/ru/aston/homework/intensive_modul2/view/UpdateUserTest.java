package ru.aston.homework.intensive_modul2.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.service.UserService;
import java.util.Optional;
import java.util.Scanner;
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
    void testInvoke_userNotFound() {

        Long userId = 2L;
        when(scanner.nextLong()).thenReturn(userId);
        when(userService.findById(userId)).thenReturn(Optional.empty());

        
        updateUser.invoke(scanner);


        verify(userService).findById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testInvoke_invalidIdFormat() {

        when(scanner.nextLong()).thenThrow(NumberFormatException.class);


        updateUser.invoke(scanner);


        verifyNoInteractions(userService);
    }

    @Test
    void testUpdateName_blankInput() {

        User user = new User();
        user.setName("John Doe");
        when(scanner.nextLine()).thenReturn("");


        updateUser.updateName(user, scanner);


        assertEquals("John Doe", user.getName());
    }

    @Test
    void testUpdateName_invalidName() {

        User user = new User();
        user.setName("John Doe");
        when(scanner.nextLine()).thenReturn("John!Doe", "John Doe");


        updateUser.updateName(user, scanner);


        assertEquals("John Doe", user.getName());
    }

    @Test
    void testUpdateEmail_blankInput() {

        User user = new User();
        user.setEmail("john@example.com");
        when(scanner.nextLine()).thenReturn("");


        updateUser.updateEmail(user, scanner);


        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void testUpdateEmail_invalidEmail() {

        User user = new User();
        user.setEmail("john@example.com");
        when(scanner.nextLine()).thenReturn("invalid-email", "john.smith@example.com");


        updateUser.updateEmail(user, scanner);


        assertEquals("john.smith@example.com", user.getEmail());
    }

    @Test
    void testUpdateAge_blankInput() {

        User user = new User();
        user.setAge(30);
        when(scanner.nextLine()).thenReturn("");


        updateUser.updateAge(user, scanner);


        assertEquals(30, user.getAge());
    }

    @Test
    void testUpdateAge_invalidAge() {

        User user = new User();
        user.setAge(30);
        when(scanner.nextLine()).thenReturn("-5", "130", "35");


        updateUser.updateAge(user, scanner);


        assertEquals(35, user.getAge());
    }

    @Test
    void testVerifyUpdate_userFound() {

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
    void testVerifyUpdate_userNotFound() {

        Long userId = 4L;
        when(userService.findById(userId)).thenReturn(Optional.empty());


        updateUser.verifyUpdate(userId);


        verify(userService).findById(userId);
    }

    @Test
    void testUpdateAge_nonNumericInput() {

        User user = new User();
        user.setAge(30);
        when(scanner.nextLine()).thenReturn("abc", "35");


        updateUser.updateAge(user, scanner);


        assertEquals(35, user.getAge());
    }

    @Test
    void testLogging_userNotFound() {

        Long userId = 2L;
        when(scanner.nextLong()).thenReturn(userId);
        when(userService.findById(userId)).thenReturn(Optional.empty());


        updateUser.invoke(scanner);


        LOGGER.info("User not found with ID: {}", userId);

    }

}
