package lab5;

import java.util.regex.*;
import java.util.Scanner;

public class Task1 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Задание 1: Поиск всех чисел в тексте ===\n");

            String exampleText = "The price of the product is $19.99";
            System.out.println("Пример текста: " + exampleText);
            findNumbers(exampleText);

            System.out.println("\n--- Дополнительные примеры ---");

            String[] testTexts = {
                "В корзине 5 яблок, 3.5 кг апельсинов и 12 бананов",
                "Температура составляет -15.7 градусов, а влажность 80%",
                "Цены: 100, 250.50, 1000.99 рублей",
                "Нет чисел в этой строке!"
            };

            for (String text : testTexts) {
                System.out.println("\nТекст: " + text);
                findNumbers(text);
            }

            System.out.println("\n--- Интерактивный режим ---");
            System.out.println("Введите ваш текст (или 'exit' для выхода):");

            while (true) {
                System.out.print("> ");
                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                findNumbers(userInput);
            }

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

   
    private static void findNumbers(String text) {
        try {
            Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
            Matcher matcher = pattern.matcher(text);

            boolean found = false;
            System.out.print("Найденные числа: ");

            while (matcher.find()) {
                System.out.print(matcher.group() + " ");
                found = true;
            }

            if (!found) {
                System.out.print("числа не найдены");
            }
            System.out.println();

        } catch (PatternSyntaxException e) {
            System.err.println("Ошибка в регулярном выражении: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка при обработке текста: " + e.getMessage());
        }
    }
}
