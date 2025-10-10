package ru.aston.homework.intensive_modul2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.homework.intensive_modul2.dao.UserDaoImpl;
import ru.aston.homework.intensive_modul2.service.UserService;
import ru.aston.homework.intensive_modul2.service.UserServiceImpl;
import ru.aston.homework.intensive_modul2.view.CreateUser;
import ru.aston.homework.intensive_modul2.view.DeleteUser;
import ru.aston.homework.intensive_modul2.view.FindUserByID;
import ru.aston.homework.intensive_modul2.view.ListAllUsers;
import ru.aston.homework.intensive_modul2.view.UpdateUser;
import ru.aston.homework.intensive_modul2.view.UserChoice;
import ru.aston.homework.intensive_modul2.view.UserChoiceStrategy;
import ru.aston.homework.intensive_modul2.util.HibernateUtil;

import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

public class Application {
    private final Map<UserChoice, UserChoiceStrategy> choiceToStrategy;
    private final UserService userService;
    private final Scanner scanner;
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public Application(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
        this.choiceToStrategy = new EnumMap<>(UserChoice.class);
        initializeStrategies();
    }

    public static void main(String[] args) {
        try {
            UserService userService = new UserServiceImpl(new UserDaoImpl());
            Application app = new Application(userService);
            app.run();
        } finally {
            HibernateUtil.shutdown();
        }
    }

    private void run() {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                UserChoice userChoice = UserChoice.fromValue(choice);

                if (userChoice == UserChoice.EXIT) {
                    running = false;
                    LOGGER.info("\033[32mThe program is over. Goodbye!\033[0m");
                    continue;
                }

                UserChoiceStrategy strategy = choiceToStrategy.get(userChoice);
                if (strategy != null) {
                    strategy.invoke(scanner);
                } else {
                    LOGGER.warn("\033[31mUnknown command\033[0m");
                }
            } catch (NumberFormatException e) {
                LOGGER.error("\033[31mInput error. Please enter a number\033[0m");
            } catch (IllegalArgumentException e) {
                LOGGER.error("\033[31mError: {}\033[0m", e.getMessage());
            } catch (Exception e) {
                LOGGER.error("\033[31mCritical error: {}\033[0m", e.getMessage());
            }
        }
        scanner.close();
    }

    private void initializeStrategies(){
        choiceToStrategy.put(UserChoice.CREATE_USER, new CreateUser(userService));
        choiceToStrategy.put(UserChoice.GET_USER_BY_ID, new FindUserByID(userService));
        choiceToStrategy.put(UserChoice.UPDATE_USER, new UpdateUser(userService));
        choiceToStrategy.put(UserChoice.DELETE_USER, new DeleteUser(userService));
        choiceToStrategy.put(UserChoice.LIST_ALL_USERS, new ListAllUsers(userService));
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
