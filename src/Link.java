import java.time.LocalDate;

class Link {
    private String originalUrl;
    private String shortUrl;
    private String description;
    private LocalDate creationDate;
    private int clickCount;
    private int clickLimit; // 0 - без лимита, -1 - заблокирована
    private String userId;
    private boolean isActive;

    public Link(String originalUrl, String description, String userId, int clickLimit) {
        this.originalUrl = originalUrl;
        this.description = description;
        this.userId = userId;
        this.creationDate = LocalDate.now();
        this.clickCount = 0;
        this.clickLimit = clickLimit;
        this.isActive = clickLimit != -1; // Ссылка активна, если не установлена блокировка
        this.shortUrl = generateShortUrl(originalUrl, userId);
    }

    // Конструктор для обратной совместимости
    public Link(String originalUrl, String description, String userId) {
        this(originalUrl, description, userId, 0);
    }

    private String generateShortUrl(String originalUrl, String userId) {
        int hash = Math.abs(originalUrl.hashCode());
        return Integer.toHexString(hash).substring(0, 2) + userId;
    }

    // Увеличиваем счетчик кликов с проверкой лимита
    public boolean incrementClickCount() {
        if (!isActive) {
            return false; // Ссылка заблокирована
        }

        if (clickLimit > 0 && clickCount >= clickLimit) {
            isActive = false; // Лимит исчерпан, блокируем ссылку
            return false;
        }

        clickCount++;

        // Проверяем, не достигли ли лимита после увеличения
        if (clickLimit > 0 && clickCount >= clickLimit) {
            isActive = false;
        }

        return true;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public int getClickCount() {
        return clickCount;
    }

    public int getClickLimit() {
        return clickLimit;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isActive() {
        return isActive;
    }

    // Установка нового лимита переходов
    public void setClickLimit(int newLimit) {
        this.clickLimit = newLimit;

        if (newLimit == -1) {
            // Блокировка ссылки
            this.isActive = false;
        } else if (newLimit == 0) {
            // Без лимита
            this.isActive = true;
        } else if (clickCount >= newLimit) {
            // Лимит уже исчерпан
            this.isActive = false;
        } else {
            // Лимит установлен, но еще не исчерпан
            this.isActive = true;
        }
    }

    @Override
    public String toString() {
        String limitInfo = clickLimit > 0 ? "/" + clickLimit : "";
        String status = isActive ? "Активна" : "Заблокирована";
        return String.format("Оригинальная ссылка: %s\nКороткая ссылка: %s\nОписание: %s\nДата создания: %s\nКликов: %d%s\nСтатус: %s",
                originalUrl, shortUrl, description, creationDate, clickCount, limitInfo, status);
    }
}
