package ru.aston.homework.intensive.notificationservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class NotificationServiceAppIntegrationTest {

    @Container
    static GenericContainer<?> mailServer = new GenericContainer<>("maildev/maildev:2.1.0")
            .withExposedPorts(1025, 1080);

    @Autowired
    private ApplicationContext applicationContext;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", mailServer::getHost);
        registry.add("spring.mail.port", () -> mailServer.getMappedPort(1025));
    }

    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }

    @Test
    void mainMethodStartsApplication() {
        NotificationServiceApp.main(new String[]{});
    }
}
