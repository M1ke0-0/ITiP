package lab5;

import java.util.Scanner;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Задание 5. Поиск всех слов, начинающихся с заданной буквы
 * Программа ищет все слова в заданном тексте, начинающиеся с заданной буквы,
 * и выводит их на экран.
 */
public class Task5 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Задание 5: Поиск слов по начальной букве ===\n");

            // Демонстрация с примерами
            System.out.println("--- Примеры поиска слов ---");

            String sampleText = "Java is a popular programming language. " +
                    "JavaScript and JSON are also important. " +
                    "Many Java developers love Java's features. " +
                    "Программирование на Java требует знания основ.";

            System.out.println("Пример текста:");
            System.out.println(sampleText + "\n");

            // Поиск слов на разные буквы
            char[] testLetters = { 'J', 'j', 'p', 'П', 'a', 'M' };

            for (char letter : testLetters) {
                findWordsByLetter(sampleText, letter);
            }

            // Дополнительные примеры
            System.out.println("\n--- Дополнительные примеры ---");

            String[] testCases = {
                    "Apple, apricot and avocado are fruits starting with A",
                    "The cat climbed the tree carefully",
                    "Быстрая бурая лиса прыгает через ленивую собаку",
                    "One two three four five six seven eight nine ten"
            };

            for (String text : testCases) {
                System.out.println("\nТекст: " + text);
                // Для демонстрации будем искать первую букву каждого текста
                if (text.length() > 0) {
                    char firstChar = text.charAt(0);
                    findWordsByLetter(text, firstChar);
                }
            }

            // Интерактивный режим
            System.out.println("\n--- Интерактивный режим ---");
            System.out.println("Введите текст для анализа (или 'exit' для выхода):");

            while (true) {
                System.out.print("Текст> ");
                String userText = scanner.nextLine();

                if (userText.equalsIgnoreCase("exit")) {
                    break;
                }

                if (userText.trim().isEmpty()) {
                    System.out.println("Текст не может быть пустым!");
                    continue;
                }

                System.out.print("Введите букву для поиска> ");
                String letterInput = scanner.nextLine();

                if (letterInput.isEmpty()) {
                    System.out.println("Буква не может быть пустой!");
                    continue;
                }

                char letter = letterInput.charAt(0);
                findWordsByLetter(userText, letter);
            }

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Находит и выводит все слова, начинающиеся с заданной буквы
     * 
     * @param text   текст для поиска
     * @param letter начальная буква
     */
    @SuppressWarnings("unused")
    private static void findWordsByLetter(String text, char letter) {
        try {
            System.out.println("\nПоиск слов, начинающихся с буквы '" + letter + "':");

            // Создаем регулярное выражение для поиска слов
            // \\b - граница слова
            // [letter] - указанная буква (с учетом регистра)
            // \\w* - остальная часть слова (буквы, цифры, подчеркивание)

            // Экранируем специальные символы регулярных выражений
            String escapedLetter = Pattern.quote(String.valueOf(letter));

            // Поиск с учетом регистра
            Pattern patternCaseSensitive = Pattern.compile(
                    "\\b" + escapedLetter + "\\w*",
                    Pattern.UNICODE_CHARACTER_CLASS);

            // Поиск без учета регистра
            Pattern patternCaseInsensitive = Pattern.compile(
                    "\\b" + escapedLetter + "\\w*",
                    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);

            // Используем поиск без учета регистра
            Matcher matcher = patternCaseInsensitive.matcher(text);

            List<String> foundWords = new ArrayList<>();
            while (matcher.find()) {
                String word = matcher.group();
                if (!foundWords.contains(word)) {
                    foundWords.add(word);
                }
            }

            if (foundWords.isEmpty()) {
                System.out.println("  Слова не найдены");
            } else {
                System.out.println("  Найдено слов: " + foundWords.size());
                System.out.println("  Список слов:");
                for (int i = 0; i < foundWords.size(); i++) {
                    System.out.println("    " + (i + 1) + ". " + foundWords.get(i));
                }
            }

        } catch (PatternSyntaxException e) {
            System.err.println("Ошибка в регулярном выражении: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка при поиске слов: " + e.getMessage());
        }
    }

    /**
     * Альтернативная версия с подсчетом вхождений каждого слова
     * 
     * @param text   текст для поиска
     * @param letter начальная буква
     */
    @SuppressWarnings("unused")
    private static void findWordsWithCount(String text, char letter) {
        try {
            System.out.println("\nПоиск слов, начинающихся с буквы '" + letter + "' (с подсчетом):");

            String escapedLetter = Pattern.quote(String.valueOf(letter));
            Pattern pattern = Pattern.compile(
                    "\\b" + escapedLetter + "\\w*",
                    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);

            Matcher matcher = pattern.matcher(text);

            List<String> allMatches = new ArrayList<>();
            while (matcher.find()) {
                allMatches.add(matcher.group());
            }

            if (allMatches.isEmpty()) {
                System.out.println("  Слова не найдены");
                return;
            }

            System.out.println("  Всего найдено: " + allMatches.size() + " вхождений");
            System.out.println("  Слова с количеством повторений:");

            // Подсчет уникальных слов
            java.util.Map<String, Integer> wordCount = new java.util.HashMap<>();
            for (String word : allMatches) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }

            for (java.util.Map.Entry<String, Integer> entry : wordCount.entrySet()) {
                System.out.println("    " + entry.getKey() + " - " + entry.getValue() + " раз(а)");
            }

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
