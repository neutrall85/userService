package ru.aston.homework.intensive_modul2.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Email '" + email + "' already exists");
    }
}

