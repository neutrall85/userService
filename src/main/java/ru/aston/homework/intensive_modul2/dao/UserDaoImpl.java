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
            if (transaction != null) {
                transaction.rollback();
            }
            throw new IllegalArgumentException("Error creating user", e);
        } finally {
            HibernateUtil.closeSession();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        Session session;
        try {
            session = HibernateUtil.getSession();
            return Optional.ofNullable(session.get(User.class, id));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error finding user by id: " + id, e);
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
        return findById(id).isPresent();
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        Session session;
        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
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
