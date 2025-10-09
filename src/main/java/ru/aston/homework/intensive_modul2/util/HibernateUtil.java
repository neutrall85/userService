package ru.aston.homework.intensive_modul2.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import ru.aston.homework.intensive_modul2.model.User;

public final class HibernateUtil {

    private HibernateUtil() { }

    private static final SessionFactory SESSION_FACTORY;
    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);

    static {
        try {
            SESSION_FACTORY = buildSessionFactory();
        } catch (Exception ex) {
            throw new IllegalArgumentException("\033[31mInitialization failed\033[0m", ex);
        }
    }

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .addAnnotatedClass(User.class)
                    .configure()
                    .buildSessionFactory();
        } catch (Exception ex) {
            if (LOGGER != null) {
                LOGGER.error("\033[31mInitial SessionFactory creation failed: {}\033[0m", ex.getMessage());
            }
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}


