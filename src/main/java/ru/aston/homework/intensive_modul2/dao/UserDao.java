package ru.aston.homework.intensive_modul2.dao;

import ru.aston.homework.intensive_modul2.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Long create(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    void update(User user);
    void delete(Long id);
    boolean exists(Long id);
}
