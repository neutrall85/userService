package ru.aston.homework.intensive.userservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.aston.homework.intensive.userservice.controller.dto.UserEvent;
import java.util.concurrent.TimeUnit;

@Service
public class KafkaEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventService.class);

    @Value("${app.kafka.topics.user-created:user-created-topic}")
    private String userCreatedTopic;

    @Value("${app.kafka.topics.user-deleted:user-deleted-topic}")
    private String userDeletedTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserEvent(String operation, String email, Long userId) {
        try {
            String topic = getTopicByOperation(operation);
            UserEvent event = new UserEvent(operation, email, userId);

            SendResult<String, Object> result = kafkaTemplate.send(topic, email, event)
                    .get(5, TimeUnit.SECONDS);

            LOGGER.info("✅ SUCCESS: Sent user event to Kafka topic '{}': {} for user: {}, partition: {}, offset: {}",
                    topic, operation, email, result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());

        } catch (Exception ex) {
            LOGGER.error("❌ FAILED to send user event to Kafka: {} for user: {}. Error: {}",
                    operation, email, ex.getMessage());
        }
    }

    private String getTopicByOperation(String operation) {
        return "CREATE".equals(operation) ? userCreatedTopic : userDeletedTopic;
    }
}
