package ru.aston.homework.intensive.notificationservice.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class EmailRequestTest {

    private final Validator validator;

    public EmailRequestTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldCreateEmailRequestWithValidData() {
        String email = "test@mail.ru";
        String subject = "Test Subject";
        String message = "Test Message";
        EmailRequest emailRequest = new EmailRequest(email, subject, message);
        assertThat(emailRequest.getEmail()).isEqualTo(email);
        assertThat(emailRequest.getSubject()).isEqualTo(subject);
        assertThat(emailRequest.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldValidateEmailRequestWithValidData() {
        EmailRequest emailRequest = new EmailRequest(
                "valid@mail.ru",
                "Valid Subject",
                "Valid Message"
        );
        Set<ConstraintViolation<EmailRequest>> violations = validator.validate(emailRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationWhenEmailIsBlank() {
        EmailRequest emailRequest = new EmailRequest(
                "",
                "Test Subject",
                "Test Message"
        );
        Set<ConstraintViolation<EmailRequest>> violations = validator.validate(emailRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Email cannot be blank");
    }

    @Test
    void shouldFailValidationWhenEmailIsInvalid() {
        EmailRequest emailRequest = new EmailRequest(
                "invalid-email",
                "Test Subject",
                "Test Message"
        );
        Set<ConstraintViolation<EmailRequest>> violations = validator.validate(emailRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Email should be valid");
    }

    @Test
    void shouldFailValidationWhenEmailIsNull() {
        EmailRequest emailRequest = new EmailRequest(
                null,
                "Test Subject",
                "Test Message"
        );
        Set<ConstraintViolation<EmailRequest>> violations = validator.validate(emailRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Email cannot be blank");
    }

    @Test
    void shouldFailValidationWhenSubjectIsBlank() {
        EmailRequest emailRequest = new EmailRequest(
                "test@mail.ru",
                "",
                "Test Message"
        );
        Set<ConstraintViolation<EmailRequest>> violations = validator.validate(emailRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Subject cannot be blank");
    }

    @Test
    void shouldFailValidationWhenSubjectIsNull() {
        EmailRequest emailRequest = new EmailRequest(
                "test@mail.ru",
                null,
                "Test Message"
        );
        Set<ConstraintViolation<EmailRequest>> violations = validator.validate(emailRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Subject cannot be blank");
    }

    @Test
    void shouldFailValidationWhenMessageIsBlank() {
        EmailRequest emailRequest = new EmailRequest(
                "test@mail.ru",
                "Test Subject",
                ""
        );
        Set<ConstraintViolation<EmailRequest>> violations = validator.validate(emailRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Message cannot be blank");
    }

    @Test
    void shouldFailValidationWhenMessageIsNull() {
        EmailRequest emailRequest = new EmailRequest(
                "test@mail.ru",
                "Test Subject",
                null
        );
        Set<ConstraintViolation<EmailRequest>> violations = validator.validate(emailRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Message cannot be blank");
    }

    @Test
    void shouldFailValidationWithMultipleErrors() {
        EmailRequest emailRequest = new EmailRequest(
                "",
                "",
                ""
        );
        Set<ConstraintViolation<EmailRequest>> violations = validator.validate(emailRequest);
        assertThat(violations).hasSize(3);
        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
        assertThat(messages).contains(
                "Email cannot be blank",
                "Subject cannot be blank",
                "Message cannot be blank"
        );
    }

    @Test
    void shouldSetAndGetPropertiesCorrectly() {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail("setter@mail.ru");
        emailRequest.setSubject("Setter Subject");
        emailRequest.setMessage("Setter Message");
        assertThat(emailRequest.getEmail()).isEqualTo("setter@mail.ru");
        assertThat(emailRequest.getSubject()).isEqualTo("Setter Subject");
        assertThat(emailRequest.getMessage()).isEqualTo("Setter Message");
    }

    @Test
    void shouldHandleNullValuesInSetters() {
        EmailRequest emailRequest = new EmailRequest("test@mail.ru", "Subject", "Message");
        emailRequest.setEmail(null);
        emailRequest.setSubject(null);
        emailRequest.setMessage(null);
        assertThat(emailRequest.getEmail()).isNull();
        assertThat(emailRequest.getSubject()).isNull();
        assertThat(emailRequest.getMessage()).isNull();
    }

    @Test
    void shouldValidateComplexEmailScenarios() {
        String[] validEmails = {
                "simple@mail.ru",
                "very.common@mail.ru",
                "disposable.style.email.with+symbol@mail.ru",
                "other.email-with-hyphen@mail.ru",
                "fully-qualified-domain@mail.ru",
                "user.name+tag+sorting@mail.ru",
                "x@mail.ru",
                "example-indeed@mail-mail.ru",
                "test@gmail.com",
                "test@mail.mail.ru"
        };
        String[] invalidEmails = {
                "plainaddress",
                "@gmail.com",
                "Gmail Contact <test@gmail.com>",
                "no-at-sign",
                "missing-domain@.com",
                "two@@gmail.com",
                "abc@def@mail.ru"
        };
        for (String validEmail : validEmails) {
            EmailRequest request = new EmailRequest(validEmail, "Subject", "Message");
            Set<ConstraintViolation<EmailRequest>> violations = validator.validate(request);
            assertThat(violations).as("Email should be valid: " + validEmail).isEmpty();
        }
        for (String invalidEmail : invalidEmails) {
            EmailRequest request = new EmailRequest(invalidEmail, "Subject", "Message");
            Set<ConstraintViolation<EmailRequest>> violations = validator.validate(request);
            assertThat(violations).as("Email should be invalid: " + invalidEmail).hasSize(1);
        }
    }
}
