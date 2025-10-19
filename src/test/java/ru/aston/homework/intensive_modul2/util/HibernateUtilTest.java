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
    void testGetSession_shouldReturnOpenSession() {
        session = HibernateUtil.getSession();
        assertTrue(session.isOpen(), "Session should be open");
    }

    @Test
    void testCloseSession_shouldCloseSession() {
        session = HibernateUtil.getSession();
        HibernateUtil.closeSession();
        assertFalse(session.isOpen(), "Session should be closed");
    }

    @Test
    void testGetSession_shouldReuseExistingSession() {
        Session firstSession = HibernateUtil.getSession();
        Session secondSession = HibernateUtil.getSession();
        assertSame(firstSession, secondSession, "Same session should be reused");
    }

    @Test
    void testSessionFactory_shouldNotBeNull() {
        assertNotNull(HibernateUtil.getSessionFactory(), "SessionFactory should not be null");
    }

    @Test
    void testThreadLocalSession_shouldBeEmptyAfterClose() {
        HibernateUtil.closeSession();
        assertNull(HibernateUtil.getSessionVar().get(), "ThreadLocal session should be null after close");
    }

}
