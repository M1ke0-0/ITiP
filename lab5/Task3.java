package lab5;

import java.util.Scanner;
import java.util.regex.*;

/**
 * Задание 3. Поиск заглавной буквы после строчной
 * Программа находит все случаи, когда сразу после строчной буквы идет заглавная
 * без какого-либо символа между ними и выделяет их знаками «!» с двух сторон.
 */
public class Task3 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Задание 3: Поиск заглавной буквы после строчной ===\n");

            System.out.println("Программа находит случаи, когда строчная буква сразу");
            System.out.println("переходит в заглавную и выделяет их знаками '!'.\n");

            // Демонстрация с примерами
            System.out.println("--- Примеры обработки текста ---");
            String[] testTexts = {
                "helloWorld",
                "iPhone и iPad от Apple",
                "thisIsACamelCaseExample",
                "HTML и CSS это основа веб-разработки",
                "JavaScript",
                "Москва Moscow",
                "testStringWithManyCamelCaseWords",
                "нет переходов в этом тексте",
                "ABCDEF",
                "abcdef"
            };

            for (String text : testTexts) {
                highlightLowerToUpper(text);
            }

            // Интерактивный режим
            System.out.println("\n--- Интерактивный режим ---");
            System.out.println("Введите текст для обработки (или 'exit' для выхода):");

            while (true) {
                System.out.print("> ");
                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                highlightLowerToUpper(userInput);
            }

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Находит и выделяет все случаи перехода строчной буквы в заглавную
     * @param text исходный текст
     */
    private static void highlightLowerToUpper(String text) {
        try {
            System.out.println("\nИсходный текст: " + text);

            // Регулярное выражение для поиска строчной буквы, за которой следует заглавная
            // ([a-zа-я]) - строчная буква (латиница или кириллица)
            // ([A-ZА-Я]) - заглавная буква (латиница или кириллица)
            Pattern pattern = Pattern.compile("([a-zа-яё])([A-ZА-ЯЁ])");
            Matcher matcher = pattern.matcher(text);

            // Подсчет количества найденных случаев
            int count = 0;
            StringBuffer result = new StringBuffer();

            while (matcher.find()) {
                count++;
                // Заменяем найденную пару на пару, обрамленную восклицательными знаками
                String replacement = "!" + matcher.group(1) + matcher.group(2) + "!";
                matcher.appendReplacement(result, replacement);
            }
            matcher.appendTail(result);

            if (count > 0) {
                System.out.println("Результат:      " + result.toString());
                System.out.println("Найдено переходов: " + count);
            } else {
                System.out.println("Результат:      " + text);
                System.out.println("Переходы не найдены");
            }

        } catch (PatternSyntaxException e) {
            System.err.println("Ошибка в регулярном выражении: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка при обработке текста: " + e.getMessage());
        }
    }

    /**
     * Альтернативная версия с детальным выводом найденных пар
     * @param text исходный текст
     */
    @SuppressWarnings("unused")
    private static void findAndDisplayPairs(String text) {
        try {
            System.out.println("\nИсходный текст: " + text);

            Pattern pattern = Pattern.compile("([a-zа-яё])([A-ZА-ЯЁ])");
            Matcher matcher = pattern.matcher(text);

            boolean found = false;
            System.out.println("Найденные пары:");

            while (matcher.find()) {
                found = true;
                System.out.println("  Позиция " + matcher.start() + ": '" +
                                 matcher.group(1) + matcher.group(2) +
                                 "' (" + matcher.group(1) + " → " + matcher.group(2) + ")");
            }

            if (!found) {
                System.out.println("  Переходы не найдены");
            }

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
