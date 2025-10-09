package ru.aston.homework.intensive_modul2.service;

import ru.aston.homework.intensive_modul2.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Long create(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    void update(User user);
    void delete(Long id);
    boolean exists(Long id);
}

