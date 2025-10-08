package ru.aston.homework.intensive_modul2.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);

    private HibernateUtil() { }

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (RuntimeException ex) {
            assert LOGGER != null;
            LOGGER.error("Initial SessionFactory creation failed: {}", ex.getMessage());
            throw new ExceptionInInitializerError();
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}

