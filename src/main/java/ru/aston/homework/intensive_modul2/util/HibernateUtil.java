package ru.aston.homework.intensive_modul2.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import ru.aston.homework.intensive_modul2.entity.User;

public final class HibernateUtil {

    private HibernateUtil() { }

    static SessionFactory sessionFactory;
    static final ThreadLocal<Session> SESSION = new ThreadLocal<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static ThreadLocal<Session> getSessionVar() {
        return SESSION;
    }

    static {
        try {
            sessionFactory = buildSessionFactory();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Initialization failed", ex);
        }
    }

    public static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .addAnnotatedClass(User.class)
                    .configure()
                    .buildSessionFactory();
        } catch (Exception e) {
            LOGGER.error("Initial SessionFactory creation failed: {}", e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Session getSession() {
        Session session = SESSION.get();
        if (session == null || !session.isOpen()) {
            session = sessionFactory.openSession();
            SESSION.set(session);
        }
        return session;
    }

    public static void closeSession() {
        Session session = SESSION.get();
        if (session != null && session.isOpen()) {
            session.close();
            SESSION.remove();
        }
    }
}
