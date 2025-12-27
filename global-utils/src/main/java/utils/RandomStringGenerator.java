package utils;

import java.time.LocalDate;
import java.util.Random;

public class RandomStringGenerator {

    private static final int length = 10;

    public static String generateRandomString() {
        LocalDate now = LocalDate.now();
        String dateString = now.toString().replace("-", "");

        String randomString = generateString();

        return dateString + randomString;
    }

    private static String generateString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return stringBuilder.toString();
    }
}
