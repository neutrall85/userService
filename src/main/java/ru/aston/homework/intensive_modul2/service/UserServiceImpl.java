package ru.aston.homework.intensive_modul2.service;

import ru.aston.homework.intensive_modul2.dao.UserDao;
import ru.aston.homework.intensive_modul2.entity.User;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Long create(User user) {
        return userDao.create(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void delete(Long id) {
        userDao.delete(id);
    }

    @Override
    public boolean exists(Long id) {
        return findById(id).isPresent();
    }
}
