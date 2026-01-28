//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Scanner;
import java.time.LocalDate;
import java.util.*;
import java.awt.Desktop;
import java.net.URI;
/*
Класс Main - тут вызываестя главное меню сервиса
Пользователь выбирает создать айди или войти по своему
 */
public class Main {
    private static LinkManager linkManager = new LinkManager();
    private static IDGenerate idGenerator = new IDGenerate(linkManager);
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в систему сокращения ссылок!");

        // Добавим несколько тестовых пользователей для демонстрации
        linkManager.registerUser("test");
        linkManager.registerUser("user");

        // Добавим тестовые ссылки

        boolean running = true;
        while (running) {
            running = showMainMenu();
        }

        scanner.close();
        System.out.println("Спасибо за использование системы!");
    }

    private static boolean showMainMenu() {
        System.out.println("\n=== Главное меню ===");
        System.out.println("1 - Уже есть ID");
        System.out.println("2 - Создать ID");
        System.out.println("0 - Выйти");
        System.out.print("Выберите действие: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                handleExistingId();
                return true;
            case "2":
                handleCreateId();
                return true;
            case "0":
                return false;
            default:
                System.out.println("Неверный выбор! Пожалуйста, попробуйте снова.");
                return true;
        }
    }

    private static void handleExistingId() {
        System.out.println("\n=== Вход по существующему ID ===");

        while (true) {
            System.out.println("Введите ваш ID (или 'назад' для возврата в главное меню): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("назад")) {
                return;
            }

            if (input.isEmpty()) {
                System.out.println("ID не может быть пустым!");
                continue;
            }

            if (linkManager.userExists(input)) {
                System.out.println("ID найден! Вход выполнен.");
                UserRoom userRoom = new UserRoom(input, linkManager);
                userRoom.showUserMenu();
                return;
            } else {
                System.out.println("ID не найден! Попробуйте снова.");
            }
        }
    }

    private static void handleCreateId() {
        System.out.println("\n=== Создание нового ID ===");

        while (true) {
            System.out.println("1 - Сгенерировать автоматически");
            System.out.println("0 - Назад в главное меню");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    String generatedId = idGenerator.getUniqueId();
                    System.out.println("Ваш новый ID: " + generatedId);
                    System.out.println("Автоматический переход в личный кабинет...");
                    UserRoom userRoom = new UserRoom(generatedId, linkManager);
                    userRoom.showUserMenu();
                    return;

                case "0":
                    return;

                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }
}