package ru.aston.homework.intensive.notificationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.aston.homework.intensive.notificationservice.dto.EmailRequest;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class NotificationControllerIntegrationTest {

    @Container
    static GenericContainer<?> kafka = new GenericContainer<>(DockerImageName.parse("confluentinc/cp-kafka:7.3.2"))
            .withExposedPorts(9092)
            .withEnv("KAFKA_LISTENERS", "PLAINTEXT://0.0.0.0:9092")
            .withEnv("KAFKA_ADVERTISED_LISTENERS", "PLAINTEXT://localhost:9092")
            .withEnv("KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR", "1");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void sendEmail_ShouldReturnSuccess_WhenValidRequest() throws Exception {
        EmailRequest emailRequest = new EmailRequest("test@example.com", "Test Subject", "Test Message");
        mockMvc.perform(post("/api/notifications/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Email sent successfully to: test@example.com"));
    }

    @Test
    void sendEmail_ShouldReturnBadRequest_WhenInvalidEmail() throws Exception {
        EmailRequest emailRequest = new EmailRequest("invalid-email", "Test Subject", "Test Message");
        mockMvc.perform(post("/api/notifications/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void sendWelcomeEmail_ShouldReturnSuccess() throws Exception {
        EmailRequest emailRequest = new EmailRequest("test@example.com", "Welcome", "Welcome message");
        mockMvc.perform(post("/api/notifications/welcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome email sent successfully to: test@example.com"));
    }

    @Test
    void sendAccountDeletedEmail_ShouldReturnSuccess() throws Exception {
        EmailRequest emailRequest = new EmailRequest("test@example.com", "Account Deleted", "Goodbye message");
        mockMvc.perform(post("/api/notifications/account-deleted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Account deletion notification sent successfully to: test@example.com"));
    }

    @Test
    void health_ShouldReturnHealthy() throws Exception {
        mockMvc.perform(post("/api/notifications/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification service is healthy"));
    }
}
