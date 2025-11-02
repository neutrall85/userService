package ru.aston.homework.intensive.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = UserServiceApp.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.properties")
class UserServiceAppIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should be loaded");
    }

    @Test
    void mainMethodStartsApplication() {
        UserServiceApp.main(new String[]{});
    }
}
