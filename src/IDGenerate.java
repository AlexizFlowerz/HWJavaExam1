import java.util.Random;
/*
Уникаольность ссылки зависит от уникальности айди
ID это случайный набор из CHARACTERS
Далее, чтобы пользователь не получил уже существующий ID есть метод generateUniqueID, который вызывается
в Main.
 */
class IDGenerate {
    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int lengthOfID = 4;
    private final Random random;
    private final LinkManager linkManager;

    public IDGenerate(LinkManager linkManager) {
        this.linkManager = linkManager;
        this.random = new Random();
    }

    private String generateID() {
        StringBuilder sb = new StringBuilder(lengthOfID);
        for (int i = 0; i < lengthOfID; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
// Выдача уникуального айди. Генерация нового айди пока есть повтор в userLinks
    public String getUniqueId() {
        String newId;
        do {
            newId = generateID();
        } while (linkManager.userExists(newId));
        linkManager.registerUser(newId);
        return newId;
    }
}