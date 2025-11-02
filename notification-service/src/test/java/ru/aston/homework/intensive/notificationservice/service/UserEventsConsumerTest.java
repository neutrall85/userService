package ru.aston.homework.intensive.notificationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import ru.aston.homework.intensive.notificationservice.dto.UserEvent;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "app.kafka.topics.user-created=user-created-topic",
        "app.kafka.topics.user-deleted=user-deleted-topic"
})
@EmbeddedKafka(topics = {"user-created-topic", "user-deleted-topic"})
class UserEventsConsumerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserEventsConsumer userEventsConsumer;

    @Test
    void consumeUserCreatedEvent_Success() {
        UserEvent event = new UserEvent();
        event.setEmail("test@mail.ru");
        event.setUserId(100L);
        userEventsConsumer.consumeUserCreatedEvent(event);
        verify(emailService, times(1))
                .sendUserCreationNotification("test@mail.ru", 100L);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    void consumeUserCreatedEvent_Exception() {
        UserEvent event = new UserEvent();
        event.setEmail("test@mail.ru");
        event.setUserId(100L);
        doThrow(new RuntimeException("Email service unavailable"))
                .when(emailService)
                .sendUserCreationNotification("test@mail.ru", 100L);
        userEventsConsumer.consumeUserCreatedEvent(event);
        verify(emailService, times(1))
                .sendUserCreationNotification("test@mail.ru", 100L);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    void consumeUserDeletedEvent_Success() {
        UserEvent event = new UserEvent();
        event.setEmail("test@mail.ru");
        event.setUserId(100L);
        userEventsConsumer.consumeUserDeletedEvent(event);
        verify(emailService, times(1))
                .sendUserDeletionNotification("test@mail.ru", 100L);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    void consumeUserDeletedEvent_Exception() {
        UserEvent event = new UserEvent();
        event.setEmail("test@mail.ru");
        event.setUserId(100L);
        doThrow(new RuntimeException("Email service unavailable"))
                .when(emailService)
                .sendUserDeletionNotification("test@mail.ru", 100L);
        userEventsConsumer.consumeUserDeletedEvent(event);
        verify(emailService, times(1))
                .sendUserDeletionNotification("test@mail.ru", 100L);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    void consumeUserCreatedEvent_WithDifferentUserData() {
        UserEvent event = new UserEvent();
        event.setEmail("another.user@mail.ru");
        event.setUserId(100L);
        userEventsConsumer.consumeUserCreatedEvent(event);
        verify(emailService, times(1))
                .sendUserCreationNotification("another.user@mail.ru", 100L);
        verifyNoMoreInteractions(emailService);
    }

    @Test
    void consumeUserDeletedEvent_WithDifferentUserData() {
        UserEvent event = new UserEvent();
        event.setEmail("another.user@mail.ru");
        event.setUserId(100L);
        userEventsConsumer.consumeUserDeletedEvent(event);
        verify(emailService, times(1))
                .sendUserDeletionNotification("another.user@mail.ru", 100L);
        verifyNoMoreInteractions(emailService);
    }
}
