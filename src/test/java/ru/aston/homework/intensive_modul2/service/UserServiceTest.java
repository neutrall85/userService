package ru.aston.homework.intensive_modul2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.homework.intensive_modul2.dao.UserDao;
import ru.aston.homework.intensive_modul2.entity.User;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        reset(userDao);

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setAge(30);
    }

    @Test
    void testCreateUserShouldReturnId() {
        when(userDao.create(any(User.class))).thenReturn(1L);

        Long result = userService.create(testUser);

        assertEquals(1L, result);
        verify(userDao).create(testUser);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void testFindById_ExistingUser_ShouldReturnUser() {
        when(userDao.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test User", result.get().getName());
        verify(userDao).findById(1L);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void TestFindById_NonExistingUser_ShouldReturnEmpty() {
        when(userDao.findById(999L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(999L);

        assertTrue(result.isEmpty());
        verify(userDao).findById(999L);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void TestFindAll_ShouldReturnUserList() {
        when(userDao.findAll()).thenReturn(List.of(testUser));

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("test@example.com", result.getFirst().getEmail());
        verify(userDao).findAll();
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void testUpdateUser_ExistingUser_ShouldCallDao() {
        userService.update(testUser);

        verify(userDao).update(testUser);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void testDeleteUser_ExistingUser_ShouldCallDao() {
        userService.delete(1L);

        verify(userDao).delete(1L);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void testExists_ShouldReturnCorrectStatus() {
        when(userDao.findById(1L)).thenReturn(Optional.of(testUser));

        assertTrue(userService.exists(1L));
        verify(userDao).findById(1L);
        verifyNoMoreInteractions(userDao);
    }
}
