import java.util.*;

class LinkManager {
    private Map<String, List<Link>> userLinks; //тут будут храниться сслыки кажждого пользователя
/*
Все храниться в Мапе userLinks
ID(String) - ключ
List - список ссылок
 */
    public LinkManager() {
        userLinks = new HashMap<>();
    }

    // Регистрация нового пользователя(запись айди в userLinks)
    public void registerUser(String userId) {
        userLinks.putIfAbsent(userId, new ArrayList<>());
    }

    // Проверка существования пользователя
    public boolean userExists(String userId) {
        return userLinks.containsKey(userId);
    }

    // Добавление ссылки с лимитом переходов
    public void addLink(String userId, String url, String description, int clickLimit) {
        registerUser(userId); // Если пользователя нет - создаем
        Link newLink = new Link(url, description, userId, clickLimit);
        userLinks.get(userId).add(newLink);
        System.out.println("Ссылка успешно добавлена!");
        System.out.println("Короткая версия: " + newLink.getShortUrl());
        if (clickLimit > 0) {
            System.out.println("Лимит переходов: " + clickLimit);
        } else if (clickLimit == 0) {
            System.out.println("Лимит переходов: без ограничений");
        }
    }


    // Получение ссылок пользователя
    public List<Link> getUserLinks(String userId) {
        return userLinks.getOrDefault(userId, new ArrayList<>());
    }

    // Проверка наличия ссылок у пользователя
    public boolean hasLinks(String userId) {
        return userLinks.containsKey(userId) && !userLinks.get(userId).isEmpty();
    }

    // Получение всех зарегистрированных пользователей
    public Set<String> getAllUsers() {
        return userLinks.keySet();
    }

    // Получение количества пользователей
    public int getUserCount() {
        return userLinks.size();
    }

    // Получение количества ссылок пользователя
    public int getUserLinksCount(String userId) {
        return getUserLinks(userId).size();
    }
}
