package ru.aston.homework.intensive_modul2.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.aston.homework.intensive_modul2.entity.User;
import ru.aston.homework.intensive_modul2.util.HibernateUtil;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    @Override
    public Long create(User user) {
        Transaction transaction = null;
        Session session;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            return user.getId();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new IllegalArgumentException("Error creating user", e);
        } finally {
            HibernateUtil.closeSession();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("User ID cannot be null or less than or equal to 0");
        }
        Session session;
        try {
            session = HibernateUtil.getSession();
            return Optional.ofNullable(session.get(User.class, id));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error searching for user by ID: " + id, e);
        } finally {
            HibernateUtil.closeSession();
        }
    }

    @Override
    public List<User> findAll() {
        Session session;
        try {
            session = HibernateUtil.getSession();
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error retrieving all users", e);
        } finally {
            HibernateUtil.closeSession();
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;
        Session session;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            User existingUser = session.find(User.class, user.getId());
            if (existingUser == null) {
                throw new IllegalArgumentException("User with ID " + user.getId() + " didn't found");
            }
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new IllegalArgumentException("Error updating user", e);
        } finally {
            HibernateUtil.closeSession();
        }
    }

    @Override
    public boolean exists(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Session session;
        try {
            session = HibernateUtil.getSession();
            return session.get(User.class, id) != null;
        } finally {
            HibernateUtil.closeSession();
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        Session session;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user == null) {
                throw new IllegalArgumentException("User with ID " + id + " didn't find");
            }
            session.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new IllegalArgumentException("Error deleting user with id: " + id, e);
        } finally {
            HibernateUtil.closeSession();
        }
    }
}
