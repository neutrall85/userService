package ru.aston.homework.intensive_modul2.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

import static ru.aston.homework.intensive_modul2.Application.userDao;

public class ListAllUsers implements UserChoiceStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListAllUsers.class);

    @Override
    public void invoke(Scanner scanner) {
        List<User> users = userDao.findAll();
        if (!users.isEmpty()) {
            LOGGER.info("All users:");
            for (User user : users) {
                LOGGER.info(""" 
                \s
                User found: \033[32m
                ID: {}
                Name: {}
                E-mail: {}
                Age: {}
                Created at: {}
                \033[0m""", user.getId(), user.getName(), user.getEmail(),
                            user.getAge(), user.getCreatedAt());
            }
        } else {
            LOGGER.info("No users found!");
        }
    }
}

