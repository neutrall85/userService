package ru.aston.homework.intensive.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {
    "ru.aston.homework.intensive.circuitbreaker",
    "ru.aston.homework.intensive.userservice"
})
public class UserServiceApp {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(UserServiceApp.class);
        application.run(args);
    }
}
