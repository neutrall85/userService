package ru.aston.homework.intensive_modul2.dao;

import jakarta.transaction.Transactional;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@Transactional
@Testcontainers
@ExtendWith(MockitoExtension.class)
class UserDaoTest {

    @Mock
    private Session session;

    @InjectMocks
    private UserDaoImpl userDao;

    @Container
    private static final PostgreSQLContainer<?> db = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"))
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @BeforeAll
    static void startContainer() {
        db.start();
    }

    @AfterAll
    static void stopContainer() {
        db.close();
    }

    @BeforeEach
    void setUp() {
        HibernateUtil.buildSessionFactory();
        System.setProperty("hibernate.connection.url", db.getJdbcUrl());
        System.setProperty("hibernate.connection.username", db.getUsername());
        System.setProperty("hibernate.connection.password", db.getPassword());
        userDao = new UserDaoImpl();
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        try (var sess = HibernateUtil.getSession()) {
            sess.beginTransaction();
            sess.createMutationQuery("DELETE FROM User")
                    .executeUpdate();

            sess.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка очистки базы данных", e);
        }
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("unique_test_email_1@example.com");
        user.setAge(25);
        Long id = userDao.create(user);
        assertNotNull(id);
        Optional<User> createdUser = userDao.findById(id);
        assertTrue(createdUser.isPresent());
        assertEquals("Test User", createdUser.get().getName());
        assertEquals("unique_test_email_1@example.com", createdUser.get().getEmail());
        assertEquals(25, createdUser.get().getAge());
    }

    @Test
    void testFindById() {
        User user = new User();
        user.setName("Find Test");
        user.setEmail("unique_find_test_email@example.com");
        user.setAge(30);
        Long id = userDao.create(user);
        Optional<User> foundUser = userDao.findById(id);
        assertTrue(foundUser.isPresent());
        assertEquals(user.getName(), foundUser.get().getName());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
        assertEquals(user.getAge(), foundUser.get().getAge());
    }

    @Test
    void testFindAll() {
        User user1 = new User();
        user1.setName("User 1");
        user1.setEmail("user1@test.com");
        user1.setAge(22);
        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail("user2@test.com");
        user2.setAge(24);
        userDao.create(user1);
        userDao.create(user2);
        List<User> allUsers = userDao.findAll();
        assertThat(allUsers)
                .extracting(User::getName, User::getEmail, User::getAge)
                .containsExactlyInAnyOrder(
                        tuple("User 1", "user1@test.com", 22),
                        tuple("User 2", "user2@test.com", 24)
                );
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setName("Update Test");
        user.setEmail("update_test_email@example.com");
        user.setAge(28);
        Long id = userDao.create(user);
        Optional<User> optionalUser = userDao.findById(id);
        assertTrue(optionalUser.isPresent());
        User updatedUser = optionalUser.get();
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated_test_email@example.com");
        updatedUser.setAge(30);
        userDao.update(updatedUser);
        User finalUser = userDao.findById(id).orElse(new User());
        assertEquals("Updated Name", finalUser.getName());
        assertEquals("updated_test_email@example.com", finalUser.getEmail());
        assertEquals(30, finalUser.getAge());
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setName("Delete Test");
        user.setEmail("delete_test_email@example.com");
        user.setAge(35);
        Long id = userDao.create(user);
        userDao.delete(id);
        Optional<User> deletedUser = userDao.findById(id);
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void testDeleteWithRollback() {
        User user = new User();
        user.setName("Rollback Test");
        user.setEmail("rollback@test.com");
        user.setAge(25);
        Long id = userDao.create(user);

        try (MockedStatic<HibernateUtil> hibernateMock = mockStatic(HibernateUtil.class)) {
            hibernateMock.when(HibernateUtil::getSession)
                    .thenReturn(session);

            when(session.get(User.class, id))
                    .thenReturn(user);

            doThrow(new RuntimeException("Симулированная ошибка"))
                    .when(session)
                    .remove(any());

            try {
                userDao.delete(id);
                fail("Ожидалось исключение");
            } catch (RuntimeException e) {

                assertTrue(userDao.exists(id));
            }
        }
    }


    @Test
    void testExists() {
        User user = new User();
        user.setName("Exists Test");
        user.setEmail("exists_test_email@example.com");
        user.setAge(40);
        Long id = userDao.create(user);
        assertTrue(userDao.exists(id));
        userDao.delete(id);
        assertFalse(userDao.exists(id));
        assertThrows(IllegalArgumentException.class, () -> userDao.exists(null));
    }

    @Test
    void testCreateUserWithNullFields() {
        User user = new User();
        user.setName(null);
        user.setEmail("unique_test_email_1@example.com");
        user.setAge(25);
        assertThrows(IllegalArgumentException.class, () -> userDao.create(user));
    }

    @Test
    void testFindByIdWithNegativeAndNullId() {
        assertThrows(IllegalArgumentException.class, () -> userDao.findById(-1L));
        assertThrows(IllegalArgumentException.class, () -> userDao.findById(null));
    }

    @Test
    void testFindAllWithEmptyDatabase() {
        List<User> allUsers = userDao.findAll();
        assertTrue(allUsers.isEmpty());
    }

    @Test
    void testUpdateNonExistingUser() {
        User user = new User();
        user.setName("Update Test");
        user.setEmail("update_test_email@example.com");
        user.setAge(28);
        user.setId(999999L);
        assertThrows(IllegalArgumentException.class, () -> userDao.update(user));
    }

    @Test
    void testDeleteNonExistingUser() {
        assertThrows(IllegalArgumentException.class, () -> userDao.delete(-1L));
    }

    @Test
    void testExistsWithNonExistingId() {
        assertFalse(userDao.exists(-1L));
    }

    @Test
    void testCreateDuplicateEmail() {
        User user1 = new User();
        user1.setName("Test User");
        user1.setEmail("duplicate_email@example.com");
        user1.setAge(25);
        userDao.create(user1);
        User user2 = new User();
        user2.setName("Another User");
        user2.setEmail("duplicate_email@example.com");
        user2.setAge(30);
        assertThrows(IllegalArgumentException.class, () -> userDao.create(user2));
    }

    @Test
    void testUpdateWithNullFields() {
        User user = new User();
        user.setName("Update Test");
        user.setEmail("update_test_email@example.com");
        user.setAge(28);
        Long id = userDao.create(user);
        Optional<User> optionalUser = userDao.findById(id);
        assertTrue(optionalUser.isPresent());
        User updatedUser = optionalUser.get();
        updatedUser.setName(null);
        assertThrows(IllegalArgumentException.class, () -> userDao.update(updatedUser));
    }

    @Test
    void testUpdateWithUnexistUser() {
        assertThrows(IllegalArgumentException.class, () -> userDao.update(null));
    }

    @Test
    void testUpdateWithActiveTransaction() {

        User user = new User();
        user.setName("Active Transaction Test");
        user.setEmail("active@test.com");
        user.setAge(25);
        Long id = userDao.create(user);

        Optional<User> optionalUser = userDao.findById(id);
        assertTrue(optionalUser.isPresent());

        User updatedUser = optionalUser.get();
        updatedUser.setName("Updated Name");

        userDao.update(updatedUser);

        User finalUser = userDao.findById(id).orElse(null);
        assertNotNull(finalUser);
        assertEquals("Updated Name", finalUser.getName());
    }

    @Test
    void testUpdateWithNoTransaction() {
            User user = new User();
            user.setName("No Transaction Test");
            user.setEmail("notransaction@test.com");
            user.setAge(25);
            Long id = userDao.create(user);

            Optional<User> optionalUser = userDao.findById(id);
            assertTrue(optionalUser.isPresent());

            User updatedUser = optionalUser.get();
            updatedUser.setName("Updated Name");

            userDao.update(updatedUser);

            User finalUser = userDao.findById(id).orElse(null);
            assertNotNull(finalUser);
            assertEquals("Updated Name", finalUser.getName());
    }

    @Test
    void testUpdateWithRollback() {

        User user = new User();
        user.setName("Rollback Test");
        user.setEmail("rollback@test.com");
        user.setAge(25);
        Long id = userDao.create(user);

        Optional<User> optionalUser = userDao.findById(id);
        assertTrue(optionalUser.isPresent());

        User updatedUser = optionalUser.get();
        updatedUser.setName("Updated Name");

        String originalName = null;

        try {
            originalName = updatedUser.getName();
            userDao.update(updatedUser);

            throw new RuntimeException("Симулированная ошибка при обновлении");
        } catch (RuntimeException e) {

            User finalUser = userDao.findById(id).orElse(null);
            assertNotNull(finalUser);
            assertEquals(originalName, finalUser.getName());
        }
    }


    @Test
    void testFindAllQueryException() {
        try (MockedStatic<HibernateUtil> hibernateMock = mockStatic(HibernateUtil.class)) {
            hibernateMock.when(HibernateUtil::getSession)
                    .thenReturn(session);
            when(session.createQuery(anyString(), any()))
                    .thenThrow(new HibernateException("Hibernate query failed"));

            try {
                userDao.findAll();
                fail("Ожидалось исключение IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                assertThat(e.getMessage()).contains("Error retrieving all users");
                assertThat(e).hasCauseInstanceOf(HibernateException.class);
            }

            hibernateMock.verify(HibernateUtil::closeSession);
        }
    }

    @Test
    void testFindAllSessionException() {
        try (MockedStatic<HibernateUtil> hibernateMock = mockStatic(HibernateUtil.class)) {

            hibernateMock.when(HibernateUtil::getSession)
                    .thenThrow(new RuntimeException("Session creation failed"));

            try {
                userDao.findAll();
                fail("Ожидалось исключение IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                assertThat(e.getMessage()).contains("Error retrieving all users");
                assertThat(e).hasCauseInstanceOf(RuntimeException.class);
            }
        }
    }

    @Test
    void testFindByIdWithDatabaseError() {

        try (MockedStatic<HibernateUtil> hibernateMock = mockStatic(HibernateUtil.class)) {
            hibernateMock.when(HibernateUtil::getSession)
                    .thenReturn(session);
            doThrow(new HibernateException("Simulate Error"))
                    .when(session)
                    .get(eq(User.class), anyLong());

            try {
                userDao.findById(1L);
                fail("Ожидалось исключение IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                assertThat(e.getMessage()).contains("Error searching for user by ID: 1");
                assertThat(e).hasCauseInstanceOf(HibernateException.class);
            }
        }

    }

    @Test
    void testFindByIdWithSessionError() {
        try (var hibernateMock = mockStatic(HibernateUtil.class)) {
            hibernateMock.when(HibernateUtil::getSession)
                    .thenThrow(new RuntimeException("Session error"));

            try {
                userDao.findById(1L);
                fail("Ожидалось исключение IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                assertThat(e.getMessage()).contains("Error searching for user by ID: 1");
                assertThat(e).hasCauseInstanceOf(RuntimeException.class);
            }
        }
    }

    @Test
    void testFindByIdWithNullSession() {
        try (var hibernateMock = mockStatic(HibernateUtil.class)) {
            hibernateMock.when(HibernateUtil::getSession)
                    .thenReturn(null);

            try {
                userDao.findById(1L);
                fail("Ожидалось исключение IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                assertThat(e.getMessage()).contains("Error searching for user by ID: 1");
            }
        }
    }

}
