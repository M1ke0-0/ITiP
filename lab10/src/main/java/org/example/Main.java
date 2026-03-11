package org.example;

import org.example.utils.StringProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Программа начала работу.");
        
        System.out.println("=== Build Passport ===");
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("build-passport.properties")) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                props.forEach((key, value) -> System.out.println(key + ": " + value));
            } else {
                System.out.println("build-passport.properties не найден.");
            }
        } catch (Exception e) {
            logger.error("Ошибка при чтении build-passport.properties", e);
        }
        System.out.println("======================\n");

        System.out.println("Добро пожаловать!");
        System.out.print("Введите строку: ");
        
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        
        String reversed = StringProcessor.process(input);
        
        System.out.println("Перевернутая строка: " + reversed);
        logger.info("Пользователь ввел строку: {}", input);
        logger.info("Результат обработки: {}", reversed);
        
        logger.info("Программа завершила работу.");
    }
}
