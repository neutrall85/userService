package ru.aston.homework.intensive_modul2.view;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserChoiceTest {

    @Test
    void testFromValueValidValues() {
        assertEquals(UserChoice.CREATE_USER, UserChoice.fromValue(1));
        assertEquals(UserChoice.GET_USER_BY_ID, UserChoice.fromValue(2));
        assertEquals(UserChoice.UPDATE_USER, UserChoice.fromValue(3));
        assertEquals(UserChoice.DELETE_USER, UserChoice.fromValue(4));
        assertEquals(UserChoice.LIST_ALL_USERS, UserChoice.fromValue(5));
        assertEquals(UserChoice.EXIT, UserChoice.fromValue(0));
    }

    @Test
    void testFromValueNegativeValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> UserChoice.fromValue(-1));
        assertTrue(exception.getMessage().contains("Incorrect value"));
    }

    @Test
    void testFromValueTooBigValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> UserChoice.fromValue(10));
        assertTrue(exception.getMessage().contains("Incorrect value"));
    }

    @Test
    void testFromValueMinValue() {
        assertEquals(UserChoice.EXIT, UserChoice.fromValue(0));
    }

    @Test
    void testFromValueMaxValue() {
        assertEquals(UserChoice.LIST_ALL_USERS, UserChoice.fromValue(5));
    }

    @Test
    void testValues() {
        UserChoice[] values = UserChoice.values();
        assertEquals(6, values.length);
        assertEquals(UserChoice.CREATE_USER, values[0]);
        assertEquals(UserChoice.GET_USER_BY_ID, values[1]);
        assertEquals(UserChoice.UPDATE_USER, values[2]);
        assertEquals(UserChoice.DELETE_USER, values[3]);
        assertEquals(UserChoice.LIST_ALL_USERS, values[4]);
        assertEquals(UserChoice.EXIT, values[5]);
    }
}
