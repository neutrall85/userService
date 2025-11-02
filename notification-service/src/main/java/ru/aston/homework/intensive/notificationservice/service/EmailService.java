package ru.aston.homework.intensive.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("no-reply");

            mailSender.send(message);
            LOGGER.info("Email successfully sent to: {}, subject: {}", to, subject);
        } catch (Exception e) {
            LOGGER.error("Failed to send email to: {}. Error: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendUserCreationNotification(String email, Long userId) {
        String subject = "Добро пожаловать!";
        String message = String.format(
                """
                        Здравствуйте! Ваш аккаунт на сайте https://test.com был успешно создан.

                        ID вашего аккаунта: %d
                        Email: %s
                        Спасибо за регистрацию!""",
            userId, email
        );

        sendEmail(email, subject, message);
    }

    public void sendUserDeletionNotification(String email, Long userId) {
        String subject = "До свидания!";
        String message = String.format(
                """
                        Здравствуйте! Ваш аккаунт на сайте https://test.com был успешно удалён.

                        ID вашего аккаунта: %d
                        Email: %s
                        Если это произошло по ошибке, пожалуйста, свяжитесь с поддержкой.""",
                userId, email
        );

        sendEmail(email, subject, message);
    }
}
