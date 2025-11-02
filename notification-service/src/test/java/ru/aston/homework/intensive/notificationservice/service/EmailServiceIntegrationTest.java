package ru.aston.homework.intensive.notificationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
class EmailServiceIntegrationTest {

    @Container
    static GenericContainer<?> kafka = new GenericContainer<>(DockerImageName.parse("confluentinc/cp-kafka:7.3.2"))
            .withExposedPorts(9092)
            .withEnv("KAFKA_LISTENERS", "PLAINTEXT://0.0.0.0:9092")
            .withEnv("KAFKA_ADVERTISED_LISTENERS", "PLAINTEXT://localhost:9092")
            .withEnv("KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR", "1");

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        // Сбрасываем мок перед каждым тестом
        reset(mailSender);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public JavaMailSender javaMailSender() {
            return mock(JavaMailSender.class);
        }
    }

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        String bootstrapServers = String.format("localhost:%d", kafka.getFirstMappedPort());
        registry.add("spring.kafka.bootstrap-servers", () -> bootstrapServers);
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> "1025");
    }

    @Test
    void sendEmail_ShouldSendEmailSuccessfully() {
        // When
        emailService.sendEmail("test@mail.ru", "Test Subject", "Test Message");

        // Then
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_ShouldThrowException_WhenMailSendingFails() {
        // Given
        doThrow(new MailSendException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));

        // When & Then
        assertThrows(RuntimeException.class, () ->
                emailService.sendEmail("test@mail.ru", "Test Subject", "Test Message")
        );

        // Также проверяем, что отправка действительно была вызвана
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendUserCreationNotification_ShouldSendWelcomeEmail() {
        // When
        emailService.sendUserCreationNotification("test@mail.ru", 123L);

        // Then
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendUserDeletionNotification_ShouldSendGoodbyeEmail() {
        // When
        emailService.sendUserDeletionNotification("test@mail.ru", 123L);

        // Then
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
