import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.awt.Desktop;
import java.net.URI;
/*
При успешном вводе ID начинают работать методы UserRoom. Этом меню для работы с ссылками пользователя
 */
class UserRoom {
    private String userId;
    private Scanner scanner;
    private LinkManager linkManager;

    public UserRoom(String userId, LinkManager linkManager) {
        this.userId = userId;
        this.linkManager = linkManager;
        this.scanner = new Scanner(System.in);
    }

    public void showUserMenu() {
        boolean inUserRoom = true;

        while (inUserRoom) {
            System.out.println("\n=== Личный кабинет ===");
            System.out.println("Здравствуйте, " + userId + "!");
            System.out.println("Всего ссылок: " + linkManager.getUserLinksCount(userId));
            System.out.println("1 - Показать информацию");
            System.out.println("2 - Добавить ссылку");
            System.out.println("3 - Показать все мои ссылки");
            System.out.println("4 - Удалить ссылку");
            System.out.println("5 - Перейти по короткой ссылке");
            System.out.println("6 - Изменить лимит переходов для ссылки");
            System.out.println("0 - Выйти в главное меню");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showUserInfo();
                    break;
                case "2":
                    addNewLink();
                    break;
                case "3":
                    showAllLinks();
                    break;
                case "4":
                    deleteLink();
                    break;
                case "5":
                    openShortLink();
                    break;
                case "6":
                    changeLinkLimit();
                    break;
                case "0":
                    inUserRoom = false;
                    System.out.println("Выход из личного кабинета...");
                    break;
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }

    private void showUserInfo() {
        System.out.println("\n=== Информация о пользователе ===");
        System.out.println("Ваш ID: " + userId);
        System.out.println("Дата регистрации: " + LocalDate.now());
        System.out.println("Количество сохраненных ссылок: " + linkManager.getUserLinksCount(userId));
        System.out.println("\nНажмите Enter для продолжения...");
        scanner.nextLine();
    }

    private void addNewLink() {
        System.out.println("\n=== Добавление новой ссылки ===");

        System.out.print("Введите ссылку (URL): ");
        String url = scanner.nextLine().trim();

        if (url.isEmpty()) {
            System.out.println("Ссылка не может быть пустой!");
            return;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        System.out.print("Введите описание ссылки: ");
        String description = scanner.nextLine().trim();

        if (description.isEmpty()) {
            description = "Без описания";
        }

        System.out.print("Введите лимит переходов (0 - без лимита): ");
        try {
            int clickLimit = Integer.parseInt(scanner.nextLine().trim());
            if (clickLimit < 0) {
                System.out.println("Лимит не может быть отрицательным! Установлен лимит 0 (без ограничений).");
                clickLimit = 0;
            }

            linkManager.addLink(userId, url, description, clickLimit);
        } catch (NumberFormatException e) {
            System.out.println("Неверный формат! Установлен лимит 0 (без ограничений).");
            linkManager.addLink(userId, url, description, 0);
        }
    }

    private void showAllLinks() {
        System.out.println("\n=== Мои сохраненные ссылки ===");

        List<Link> links = linkManager.getUserLinks(userId);

        if (links.isEmpty()) {
            System.out.println("У вас пока нет сохраненных ссылок.");
        } else {
            System.out.println("Всего ссылок: " + links.size());
            System.out.println("==============================");

            for (int i = 0; i < links.size(); i++) {
                Link link = links.get(i);
                String status = link.isActive() ? "Активна" : " Заблокирована";
                System.out.println("\n[" + (i + 1) + "] " + link.getDescription() + " [" + status + "]");
                System.out.println("Короткая ссылка: " + link.getShortUrl());
                System.out.println("Оригинальная ссылка: " + link.getOriginalUrl());
                System.out.println("Дата добавления: " + link.getCreationDate());
                System.out.println("Кликов: " + link.getClickCount() +
                        (link.getClickLimit() > 0 ? "/" + link.getClickLimit() : " (без лимита)"));
                System.out.println("----------------------------");
            }
        }

        System.out.println("\nНажмите Enter для продолжения...");
        scanner.nextLine();
    }

    private void deleteLink() {
        System.out.println("\n=== Удаление ссылки ===");

        List<Link> links = linkManager.getUserLinks(userId);

        if (links.isEmpty()) {
            System.out.println("У вас нет ссылок для удаления.");
            return;
        }

        showAllLinks();

        System.out.print("Введите номер ссылки для удаления (или 0 для отмены): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 0) {
                System.out.println("Удаление отменено.");
                return;
            }

            if (choice < 1 || choice > links.size()) {
                System.out.println("Неверный номер ссылки!");
                return;
            }

            Link linkToRemove = links.get(choice - 1);
            links.remove(choice - 1);
            System.out.println("Ссылка '" + linkToRemove.getDescription() + "' успешно удалена!");

        } catch (NumberFormatException e) {
            System.out.println("Пожалуйста, введите число!");
        }
    }

    private void openShortLink() {
        System.out.println("\n=== Переход по короткой ссылке ===");

        List<Link> links = linkManager.getUserLinks(userId);

        if (links.isEmpty()) {
            System.out.println("У вас пока нет сохраненных ссылок.");
            System.out.println("\nНажмите Enter для продолжения...");
            scanner.nextLine();
            return;
        }

        // Показываем список всех коротких ссылок пользователя
        System.out.println("Ваши короткие ссылки:");
        for (Link link : links) {
            String status = link.isActive() ? "✓" : "✗";
            System.out.println("  • [" + status + "] " + link.getShortUrl() + " → " +
                    link.getDescription() + " (" + link.getClickCount() +
                    (link.getClickLimit() > 0 ? "/" + link.getClickLimit() + ")" : ")"));
        }
        System.out.println();

        System.out.print("Введите короткую ссылку для перехода (или 'назад' для отмены): ");
        String shortUrlInput = scanner.nextLine().trim();

        if (shortUrlInput.equalsIgnoreCase("назад")) {
            System.out.println("Отмена перехода по ссылке.");
            return;
        }

        // Ищем ссылку по короткому URL
        Link foundLink = null;
        for (Link link : links) {
            if (link.getShortUrl().equalsIgnoreCase(shortUrlInput)) {
                foundLink = link;
                break;
            }
        }

        if (foundLink == null) {
            System.out.println("Короткая ссылка не найдена! Проверьте правильность ввода.");
            System.out.println("Убедитесь, что вы вводите именно короткую ссылку (например: " +
                    (links.size() > 0 ? links.get(0).getShortUrl() : "пример") + ")");
        } else {
            // Проверяем активность ссылки
            if (!foundLink.isActive()) {
                System.out.println("\n Ссылка заблокирована!");
                System.out.println("Причина: лимит переходов исчерпан.");
                System.out.println("Текущее количество переходов: " + foundLink.getClickCount());
                if (foundLink.getClickLimit() > 0) {
                    System.out.println("Лимит переходов: " + foundLink.getClickLimit());
                }
                System.out.println("\nДля разблокировки измените лимит переходов в меню управления ссылками.");
            } else {
                // Получаем оригинальный URL
                String urlToOpen = foundLink.getOriginalUrl();

                System.out.println("\nИнформация о ссылке:");
                System.out.println("Короткая ссылка: " + foundLink.getShortUrl());
                System.out.println("Описание: " + foundLink.getDescription());
                System.out.println("Оригинальная ссылка: " + urlToOpen);
                System.out.println("Текущее количество переходов: " + foundLink.getClickCount());
                if (foundLink.getClickLimit() > 0) {
                    System.out.println("Лимит переходов: " + foundLink.getClickLimit());
                    System.out.println("Осталось переходов: " + (foundLink.getClickLimit() - foundLink.getClickCount()));
                }

                // Пытаемся открыть ссылку в браузере
                try {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        System.out.print("\nОткрыть ссылку в браузере? (да/нет): ");
                        String confirm = scanner.nextLine().trim().toLowerCase();

                        if (confirm.equals("да") || confirm.equals("y") || confirm.equals("yes")) {
                            // Увеличиваем счетчик кликов
                            boolean incrementSuccessful = foundLink.incrementClickCount();

                            if (incrementSuccessful) {
                                Desktop.getDesktop().browse(new URI(urlToOpen));
                                System.out.println("Браузер успешно запущен!");
                                System.out.println("Счетчик переходов увеличен.");

                                // Проверяем, не достигли ли лимита
                                if (foundLink.getClickLimit() > 0 &&
                                        foundLink.getClickCount() >= foundLink.getClickLimit()) {
                                    System.out.println("\n Внимание! Лимит переходов по этой ссылке исчерпан.");
                                    System.out.println("Ссылка будет заблокирована для дальнейшего использования.");
                                }
                            } else {
                                System.out.println("Невозможно увеличить счетчик. Ссылка заблокирована!");
                            }
                        } else {
                            System.out.println("Ссылка не была открыта. Вы можете скопировать её вручную: " + urlToOpen);
                        }
                    } else {
                        System.out.println("К сожалению, открытие ссылок в браузере не поддерживается в данной системе.");
                        System.out.println("Ссылка для копирования: " + urlToOpen);
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка при открытии ссылки: " + e.getMessage());
                    System.out.println("Вы можете скопировать ссылку вручную: " + urlToOpen);
                }
            }
        }

        System.out.println("\nНажмите Enter для продолжения...");
        scanner.nextLine();
    }

    private void changeLinkLimit() {
        System.out.println("\n=== Изменение лимита переходов для ссылки ===");

        List<Link> links = linkManager.getUserLinks(userId);

        if (links.isEmpty()) {
            System.out.println("У вас пока нет сохраненных ссылок.");
            System.out.println("\nНажмите Enter для продолжения...");
            scanner.nextLine();
            return;
        }

        showAllLinks();

        System.out.print("Введите номер ссылки для изменения лимита (или 0 для отмены): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 0) {
                System.out.println("Изменение отменено.");
                return;
            }

            if (choice < 1 || choice > links.size()) {
                System.out.println("Неверный номер ссылки!");
                return;
            }

            Link selectedLink = links.get(choice - 1);

            System.out.println("\nВыбрана ссылка: " + selectedLink.getDescription());
            System.out.println("Текущий лимит: " +
                    (selectedLink.getClickLimit() > 0 ? selectedLink.getClickLimit() : "без лимита"));
            System.out.println("Текущее количество переходов: " + selectedLink.getClickCount());

            System.out.print("Введите новый лимит переходов (0 - без лимита, -1 - заблокировать): ");
            int newLimit = Integer.parseInt(scanner.nextLine().trim());

            if (newLimit < -1) {
                System.out.println("Некорректное значение!");
                return;
            }

            selectedLink.setClickLimit(newLimit);

            if (newLimit == 0) {
                System.out.println("Лимит переходов снят. Ссылка активна без ограничений.");
            } else if (newLimit == -1) {
                System.out.println("Ссылка заблокирована.");
            } else if (selectedLink.getClickCount() >= newLimit) {
                System.out.println("Лимит установлен. Ссылка заблокирована, так как текущее количество переходов (" +
                        selectedLink.getClickCount() + ") уже превышает или равно новому лимиту (" + newLimit + ").");
            } else {
                System.out.println("Лимит установлен. Ссылка активна. Осталось переходов: " +
                        (newLimit - selectedLink.getClickCount()));
            }

        } catch (NumberFormatException e) {
            System.out.println("Пожалуйста, введите число!");
        }

        System.out.println("\nНажмите Enter для продолжения...");
        scanner.nextLine();
    }
}