package ru.aston.homework.intensive_modul2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.homework.intensive_modul2.service.UserService;
import ru.aston.homework.intensive_modul2.service.UserServiceImpl;
import ru.aston.homework.intensive_modul2.dao.UserDaoImpl;
import ru.aston.homework.intensive_modul2.view.CreateUser;
import ru.aston.homework.intensive_modul2.view.DeleteUser;
import ru.aston.homework.intensive_modul2.view.FindUserByID;
import ru.aston.homework.intensive_modul2.view.ListAllUsers;
import ru.aston.homework.intensive_modul2.view.UpdateUser;
import ru.aston.homework.intensive_modul2.view.UserChoice;
import ru.aston.homework.intensive_modul2.view.UserChoiceStrategy;
import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

public class Application {
    static Logger logger = LoggerFactory.getLogger(Application.class);
    private final UserService userService;
    private final Map<UserChoice, UserChoiceStrategy> choiceToStrategy;
    int wrongAttempts = 0;

    public Application(UserService userService) {
        this.userService = userService;
        this.choiceToStrategy = new EnumMap<>(UserChoice.class);
        initializeStrategies();
    }

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl(new UserDaoImpl());
        Scanner scanner = new Scanner(System.in);
        Application app = new Application(userService);
        app.run(scanner);
    }

    public void run(Scanner scanner) {
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = getUserChoice(scanner);
                UserChoice userChoice = UserChoice.fromValue(choice);
                if (userChoice == UserChoice.EXIT) {
                    running = false;
                    logger.info("The program is over. Goodbye!");
                    continue;
                }
                UserChoiceStrategy strategy = choiceToStrategy.get(userChoice);
                if (strategy != null) {
                    strategy.invoke(scanner);
                    wrongAttempts = 0;
                } else {
                    handleWrongAttempt(choice);
                }
            } catch (NumberFormatException e) {
                handleWrongAttempt(null);
            } catch (IllegalArgumentException e) {
                handleWrongAttempt(e.getMessage());
            } catch (Exception e) {
                logger.error("Critical error occurred: {}", e.getMessage());
            }

            if (wrongAttempts >= 3) {
                logger.error("Too many incorrect attempts. Exiting the program.");
                System.exit(1);
            }
        }
    }

    void handleWrongAttempt(Object message) {
        wrongAttempts++;
        logger.warn("Invalid choice: {}. Attempts left: {}", message, 3 - wrongAttempts);
    }

    int getUserChoice(Scanner scanner) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }
        return Integer.parseInt(input);
    }

    void initializeStrategies() {
        choiceToStrategy.put(UserChoice.CREATE_USER, new CreateUser(userService));
        choiceToStrategy.put(UserChoice.GET_USER_BY_ID, new FindUserByID(userService));
        choiceToStrategy.put(UserChoice.UPDATE_USER, new UpdateUser(userService));
        choiceToStrategy.put(UserChoice.DELETE_USER, new DeleteUser(userService));
        choiceToStrategy.put(UserChoice.LIST_ALL_USERS, new ListAllUsers(userService));
    }

    void printMenu() {
        logger.info("""
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
