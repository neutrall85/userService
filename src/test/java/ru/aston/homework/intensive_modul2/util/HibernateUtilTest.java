package ru.aston.homework.intensive_modul2.util;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HibernateUtilTest {

    Session session;

    @BeforeEach
    void setUp() {
        session = HibernateUtil.getSession();
    }

    @AfterEach
    void tearDown() {
        HibernateUtil.buildSessionFactory();
    }

    @Test
    void testGetSessionShouldReturnOpenSession() {
        session = HibernateUtil.getSession();
        assertTrue(session.isOpen(), "Session should be open");
    }

    @Test
    void testCloseSessionShouldCloseSession() {
        session = HibernateUtil.getSession();
        HibernateUtil.closeSession();
        assertFalse(session.isOpen(), "Session should be closed");
    }

    @Test
    void testGetSessionShouldReuseExistingSession() {
        Session firstSession = HibernateUtil.getSession();
        Session secondSession = HibernateUtil.getSession();
        assertSame(firstSession, secondSession, "Same session should be reused");
    }

    @Test
    void testSessionFactoryShouldNotBeNull() {
        assertNotNull(HibernateUtil.getSessionFactory(), "SessionFactory should not be null");
    }

    @Test
    void testThreadLocalSessionShouldBeEmptyAfterClose() {
        HibernateUtil.closeSession();
        assertNull(HibernateUtil.getSessionVar().get(), "ThreadLocal session should be null after close");
    }

    @Test
    void testGetSessionFactoryShouldNotBeNullAfterBuild() {

        HibernateUtil.buildSessionFactory();
        assertNotNull(HibernateUtil.getSessionFactory(), "SessionFactory should not be null after build");
    }

    @Test
    void testGetSessionShouldCreateNewSessionIfClosed() {
        Session initialSession = HibernateUtil.getSession();
        HibernateUtil.closeSession();

        Session newSession = HibernateUtil.getSession();
        assertNotSame(initialSession, newSession, "New session should be created after close");
    }

    @Test
    void testStaticInitializationShouldNotThrowException() {

        try {
            Class.forName("ru.aston.homework.intensive_modul2.util.HibernateUtil");
        } catch (ClassNotFoundException e) {
            fail("Static initialization should not throw exception");
        }
    }

    @Test
    void testCloseSessionShouldNotThrowOnNull() {
        HibernateUtil.getSessionVar().remove();
        HibernateUtil.closeSession();
        assertNull(HibernateUtil.getSessionVar().get(), "Session should be removed");
    }

    @Test
    void testGetSessionShouldHandleSessionCloseDuringGet() {
        try {
            session = HibernateUtil.getSession();
            session.close();
            HibernateUtil.getSession();
        } catch (Exception e) {
            fail("Should handle session close during get");
        }
    }
}

