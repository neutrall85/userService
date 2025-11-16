package ru.aston.homework.intensive.circuitbreaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CircuitBreakerApp {
    public static void main(String[] args) {
        SpringApplication.run(CircuitBreakerApp.class, args);
    }
}
