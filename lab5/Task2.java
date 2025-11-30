package lab5;

import java.util.Scanner;
import java.util.regex.*;

/**
 * Задание 2. Проверка корректности ввода пароля
 * Пароль должен:
 * - Состоять из латинских букв и цифр
 * - Быть длиной от 8 до 16 символов
 * - Содержать хотя бы одну заглавную букву
 * - Содержать хотя бы одну цифру
 */
public class Task2 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Задание 2: Проверка корректности пароля ===\n");

            System.out.println("Требования к паролю:");
            System.out.println("- Длина от 8 до 16 символов");
            System.out.println("- Только латинские буквы и цифры");
            System.out.println("- Минимум одна заглавная буква");
            System.out.println("- Минимум одна цифра\n");

            // Демонстрация с примерами
            System.out.println("--- Примеры проверки паролей ---");
            String[] testPasswords = {
                "Password1",      // Корректный
                "MyPass123",      // Корректный
                "weakpass",       // Нет заглавной буквы и цифры
                "NODIGITS",       // Нет цифры
                "NoDigit",        // Нет цифры
                "short1A",        // Слишком короткий (7 символов)
                "TooLongPassword12345", // Слишком длинный (21 символ)
                "Pass123!",       // Содержит спецсимвол
                "Пароль123",      // Содержит кириллицу
                "ValidPass99"     // Корректный
            };

            for (String password : testPasswords) {
                validatePassword(password);
            }

            // Интерактивный режим
            System.out.println("\n--- Интерактивный режим ---");
            System.out.println("Введите пароль для проверки (или 'exit' для выхода):");

            while (true) {
                System.out.print("> ");
                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                validatePassword(userInput);
            }

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Проверяет корректность пароля согласно требованиям
     * @param password пароль для проверки
     */
    private static void validatePassword(String password) {
        try {
            System.out.println("\nПроверка пароля: \"" + password + "\"");

            // Проверка длины
            if (password.length() < 8 || password.length() > 16) {
                System.out.println("❌ НЕВЕРНО: длина должна быть от 8 до 16 символов (текущая: " + password.length() + ")");
                return;
            }

            // Проверка на только латинские буквы и цифры
            Pattern latinAndDigitsOnly = Pattern.compile("^[a-zA-Z0-9]+$");
            if (!latinAndDigitsOnly.matcher(password).matches()) {
                System.out.println("❌ НЕВЕРНО: пароль должен содержать только латинские буквы и цифры");
                return;
            }

            // Проверка наличия хотя бы одной заглавной буквы
            Pattern hasUpperCase = Pattern.compile("[A-Z]");
            if (!hasUpperCase.matcher(password).find()) {
                System.out.println("❌ НЕВЕРНО: пароль должен содержать хотя бы одну заглавную букву");
                return;
            }

            // Проверка наличия хотя бы одной цифры
            Pattern hasDigit = Pattern.compile("\\d");
            if (!hasDigit.matcher(password).find()) {
                System.out.println("❌ НЕВЕРНО: пароль должен содержать хотя бы одну цифру");
                return;
            }

            // Все проверки пройдены
            System.out.println("✅ ВЕРНО: пароль соответствует всем требованиям");

        } catch (PatternSyntaxException e) {
            System.err.println("Ошибка в регулярном выражении: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка при проверке пароля: " + e.getMessage());
        }
    }

    /**
     * Альтернативная версия с одним регулярным выражением
     * @param password пароль для проверки
     * @return true если пароль валиден
     */
    @SuppressWarnings("unused")
    private static boolean validatePasswordOneRegex(String password) {
        try {
            // Комплексное регулярное выражение для всех требований
            // (?=.*[A-Z]) - хотя бы одна заглавная буква
            // (?=.*\d) - хотя бы одна цифра
            // [a-zA-Z0-9]{8,16} - только латинские буквы и цифры, длина 8-16
            Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)[a-zA-Z0-9]{8,16}$");
            return pattern.matcher(password).matches();
        } catch (Exception e) {
            System.err.println("Ошибка при проверке: " + e.getMessage());
            return false;
        }
    }
}
