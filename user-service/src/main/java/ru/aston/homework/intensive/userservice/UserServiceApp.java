package ru.aston.homework.intensive.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApp {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(UserServiceApp.class);
        application.run(args);
    }
}
