package ru.aston.homework.intensive.notificationservice.dto;

import java.time.LocalDateTime;

public class UserEvent {
    private String operation;
    private String email;
    private LocalDateTime timestamp;
    private Long userId;

    public UserEvent() {
    }

    public UserEvent(String operation, String email, Long userId) {
        this.operation = operation;
        this.email = email;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return String.format("""
                UserEvent{
                    operation='%s',
                    email='%s',
                    timestamp=%s,
                    userId=%d
                }""", operation, email, timestamp.toString(), userId);
    }
}
