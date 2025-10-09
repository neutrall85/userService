package ru.aston.homework.intensive_modul2.view;

public enum UserChoice {
    CREATE_USER(1),
    GET_USER_BY_ID(2),
    UPDATE_USER(3),
    DELETE_USER(4),
    LIST_ALL_USERS(5),
    EXIT(0);

    private final int val;

    UserChoice(int val) {
        this.val = val;
    }

    public static UserChoice fromValue(int value) {
        for (UserChoice choice : UserChoice.values()) {
            if (choice.val == value) {
                return choice;
            }
        }
        throw new IllegalArgumentException("\033[31mIncorrect value: \033[0m" + value);
    }
}
