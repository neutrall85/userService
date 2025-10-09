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
import ru.aston.homework.intensive_modul2.util.HibernateUtil;

import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

public class Application {
    private static final Map<UserChoice, UserChoiceStrategy> CHOICE_TO_STRATEGY = new EnumMap<>(UserChoice.class);
    public static final UserDao USER_DAO = new UserDaoImpl();
    public static final Scanner SCANNER = new Scanner(System.in);
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    static {
        CHOICE_TO_STRATEGY.put(UserChoice.CREATE_USER, new CreateUser());
        CHOICE_TO_STRATEGY.put(UserChoice.GET_USER_BY_ID, new FindUserByID());
        CHOICE_TO_STRATEGY.put(UserChoice.UPDATE_USER, new UpdateUser());
        CHOICE_TO_STRATEGY.put(UserChoice.DELETE_USER, new DeleteUser());
        CHOICE_TO_STRATEGY.put(UserChoice.LIST_ALL_USERS, new ListAllUsers());
    }


    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            String input = SCANNER.nextLine();
            UserChoice choice;

            try {
                int inputValue = Integer.parseInt(input);

                if (inputValue < 0 || inputValue > 5) {
                    throw new IllegalArgumentException("\033[31mThe value must be between 0 and 5\033[0m");
                }

                choice = UserChoice.fromValue(inputValue);

                if (choice == UserChoice.EXIT) {
                    running = false;
                    LOGGER.info("\033[32mThe program is complete. Goodbye!\033[0m");
                    break;
                }


                UserChoiceStrategy strategy = CHOICE_TO_STRATEGY.get(choice);
                strategy.invoke(SCANNER);

            } catch (NumberFormatException e) {
                LOGGER.info("\033[31mInvalid input format. Please try again.\033[0m");
            } catch (IllegalArgumentException e) {
                LOGGER.info("\033[31mError: {}\033[0m", e.getMessage());
            } catch (Exception e) {
                LOGGER.error("\033[31mAn error has occurred: {}\033[0m", e.getMessage());
            }
        }
        SCANNER.close();
        HibernateUtil.shutdown();
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
