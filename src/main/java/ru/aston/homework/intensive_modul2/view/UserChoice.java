package ru.aston.homework.intensive_modul2.view;

public enum UserChoice {
    CREATE_USER(1),
    GET_USER_BY_ID(2),
    UPDATE_USER(3),
    DELETE_USER(4),
    LIST_ALL_USERS(5),
    EXIT(0);

    private final int value;

    UserChoice(int value) {
        this.value = value;
    }

    public static UserChoice fromValue(int value) {
        return java.util.stream.Stream.of(UserChoice.values())
                .filter(choice -> choice.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Incorrect value: " + value));
    }
}
