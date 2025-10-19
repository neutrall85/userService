package ru.aston.homework.intensive_modul2.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAllUsersTest {

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        Mockito.reset(userService);
    }

    @Test
    void invoke_withUsers() {
        UserService mockUserService = Mockito.mock(UserService.class);
        ListAllUsers testListAllUsers = new ListAllUsers(mockUserService);

        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAge(30);
        user.setCreatedAt(LocalDateTime.now());

        when(mockUserService.findAll()).thenReturn(List.of(user));

        testListAllUsers.invoke(new Scanner(System.in));
        verify(mockUserService).findAll();
    }

    @Test
    void invoke_emptyList() {
        UserService mockUserService = Mockito.mock(UserService.class);
        ListAllUsers testListAllUsers = new ListAllUsers(mockUserService);

        when(mockUserService.findAll()).thenReturn(List.of());

        testListAllUsers.invoke(new Scanner(System.in));
        verify(mockUserService).findAll();
    }

    @Test
    void invoke_multipleUsers() {
        UserService mockUserService = Mockito.mock(UserService.class);
        ListAllUsers testListAllUsers = new ListAllUsers(mockUserService);

        User user1 = new User();
        user1.setId(1L);
        user1.setName("User 1");
        user1.setEmail("user1@example.com");
        user1.setAge(25);
        user1.setCreatedAt(LocalDateTime.now());

        User user2 = new User();
        user2.setId(2L);
        user2.setName("User 2");
        user2.setEmail("user2@example.com");
        user2.setAge(35);
        user2.setCreatedAt(LocalDateTime.now());

        when(mockUserService.findAll()).thenReturn(List.of(user1, user2));

        testListAllUsers.invoke(new Scanner(System.in));
        verify(mockUserService).findAll();
    }
}
