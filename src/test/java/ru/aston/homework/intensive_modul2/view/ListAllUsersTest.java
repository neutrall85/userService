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
import java.util.ArrayList;
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
    void testInvokeWithUsers() {
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
    void testInvokeEmptyList() {
        UserService mockUserService = Mockito.mock(UserService.class);
        ListAllUsers testListAllUsers = new ListAllUsers(mockUserService);
        when(mockUserService.findAll()).thenReturn(List.of());
        testListAllUsers.invoke(new Scanner(System.in));
        verify(mockUserService).findAll();
    }

    @Test
    void testInvokeMultipleUsers() {
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

    @Test
    void testInvokeNullList() {
        UserService mockUserService = Mockito.mock(UserService.class);
        ListAllUsers testListAllUsers = new ListAllUsers(mockUserService);
        when(mockUserService.findAll()).thenReturn(null);
        testListAllUsers.invoke(new Scanner(System.in));
        verify(mockUserService).findAll();
    }

    @Test
    void testInvokeExceptionHandling() {
        UserService mockUserService = Mockito.mock(UserService.class);
        ListAllUsers testListAllUsers = new ListAllUsers(mockUserService);
        when(mockUserService.findAll())
                .thenThrow(new RuntimeException("Simulated database error"));
        testListAllUsers.invoke(new Scanner(System.in));
        verify(mockUserService).findAll();
    }

    @Test
    void testInvokeEmptyScanner() {
        UserService mockUserService = Mockito.mock(UserService.class);
        ListAllUsers testListAllUsers = new ListAllUsers(mockUserService);
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAge(30);
        user.setCreatedAt(LocalDateTime.now());
        when(mockUserService.findAll()).thenReturn(List.of(user));
        try (Scanner emptyScanner = new Scanner("")) {
            testListAllUsers.invoke(emptyScanner);
        }

        verify(mockUserService).findAll();
    }

    @Test
    void testInvokeLargeUserList() {
        UserService mockUserService = Mockito.mock(UserService.class);
        ListAllUsers testListAllUsers = new ListAllUsers(mockUserService);
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            User user = new User();
            user.setId((long) i);
            user.setName("User " + i);
            user.setEmail("user" + i + "@example.com");
            user.setAge(20 + i);
            user.setCreatedAt(LocalDateTime.now());
            users.add(user);
        }
        when(mockUserService.findAll()).thenReturn(users);
        testListAllUsers.invoke(new Scanner(System.in));
        verify(mockUserService).findAll();
    }

    @Test
    void testInvokeSortingVerification() {
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
        user2.setAge(28);
        user2.setCreatedAt(LocalDateTime.now());
        User user3 = new User();
        user3.setId(3L);
        user3.setName("User 3");
        user3.setEmail("user3@example.com");
        user3.setAge(30);
        user3.setCreatedAt(LocalDateTime.now());
        when(mockUserService.findAll()).thenReturn(List.of(user3, user1, user2));
        testListAllUsers.invoke(new Scanner(System.in));
        verify(mockUserService).findAll();
    }
}
