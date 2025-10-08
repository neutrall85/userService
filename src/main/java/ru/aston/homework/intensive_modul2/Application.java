package ru.aston.homework.intensive_modul2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.homework.intensive_modul2.dao.UserDao;
import ru.aston.homework.intensive_modul2.dao.UserDaoImpl;
import ru.aston.homework.intensive_modul2.model.CreateUser;
import ru.aston.homework.intensive_modul2.model.DeleteUser;
import ru.aston.homework.intensive_modul2.model.FindUserByID;
import ru.aston.homework.intensive_modul2.model.ListAllUsers;
import ru.aston.homework.intensive_modul2.model.UpdateUser;
import ru.aston.homework.intensive_modul2.model.UserChoice;
import ru.aston.homework.intensive_modul2.model.UserChoiceStrategy;

import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

public class Application {
    private static final Map<UserChoice, UserChoiceStrategy> choiceToStrategy = new EnumMap<>(UserChoice.class);
    public static final UserDao userDao = new UserDaoImpl();
    public static final Scanner scanner = new Scanner(System.in);
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    static {
        choiceToStrategy.put(UserChoice.CREATE_USER, new CreateUser());
        choiceToStrategy.put(UserChoice.GET_USER_BY_ID, new FindUserByID());
        choiceToStrategy.put(UserChoice.UPDATE_USER, new UpdateUser());
        choiceToStrategy.put(UserChoice.DELETE_USER, new DeleteUser());
        choiceToStrategy.put(UserChoice.LIST_ALL_USERS, new ListAllUsers());
    }


    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            String input = scanner.nextLine();
            UserChoice choice;

            try {
                int inputValue = Integer.parseInt(input);

                if (inputValue < 0 || inputValue > 5) {
                    throw new IllegalArgumentException("Значение должно быть от 0 до 5");
                }

                choice = UserChoice.fromValue(inputValue);

                if (choice == UserChoice.EXIT) {
                    running = false;
                    LOGGER.info("Программа завершена. До свидания!");
                    break;
                }


                UserChoiceStrategy strategy = choiceToStrategy.get(choice);
                strategy.invoke(scanner);

            } catch (NumberFormatException e) {
                LOGGER.info("Неверный формат ввода. Попробуйте еще раз.");
            } catch (IllegalArgumentException e) {
                LOGGER.info("Ошибка: {}", e.getMessage());
            } catch (Exception e) {
                LOGGER.error("Произошла ошибка: {}", e.getMessage());
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        LOGGER.info("""
           
           User Management System:
            1. Create User
            2. Find User by ID
            3. Update User
            4. Delete User
            5. List All Users
            0. Exit
            Enter your choice:""");
    }
}
