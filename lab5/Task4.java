package lab5;

import java.util.Scanner;
import java.util.regex.*;

/**
 * Задание 4. Проверка корректности ввода IP-адреса
 * IP-адрес должен:
 * - Состоять из 4 чисел, разделенных точками
 * - Каждое число должно быть в диапазоне от 0 до 255
 */
public class Task4 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Задание 4: Проверка корректности IP-адреса ===\n");

            System.out.println("Требования к IP-адресу:");
            System.out.println("- Формат: XXX.XXX.XXX.XXX");
            System.out.println("- Каждое число в диапазоне от 0 до 255\n");

            // Демонстрация с примерами
            System.out.println("--- Примеры проверки IP-адресов ---");
            String[] testIPs = {
                "192.168.1.1",       // Корректный
                "255.255.255.255",   // Корректный (максимальный)
                "0.0.0.0",           // Корректный (минимальный)
                "127.0.0.1",         // Корректный (localhost)
                "256.1.1.1",         // Неверно: число > 255
                "192.168.1.256",     // Неверно: число > 255
                "192.168.1",         // Неверно: только 3 октета
                "192.168.1.1.1",     // Неверно: 5 октетов
                "192.168.-1.1",      // Неверно: отрицательное число
                "192.168.1.abc",     // Неверно: не число
                "192.168.01.1",      // Неверно: ведущий ноль
                "192.168.1.1 ",      // Неверно: пробел в конце
                " 192.168.1.1",      // Неверно: пробел в начале
                "192 168 1 1",       // Неверно: пробелы вместо точек
                "8.8.8.8",           // Корректный (Google DNS)
                "10.0.0.1",          // Корректный
                "172.16.0.1",        // Корректный
                "300.300.300.300"    // Неверно: все числа > 255
            };

            for (String ip : testIPs) {
                validateIP(ip);
            }

            // Интерактивный режим
            System.out.println("\n--- Интерактивный режим ---");
            System.out.println("Введите IP-адрес для проверки (или 'exit' для выхода):");

            while (true) {
                System.out.print("> ");
                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                validateIP(userInput);
            }

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Проверяет корректность IP-адреса
     * @param ip IP-адрес для проверки
     */
    private static void validateIP(String ip) {
        try {
            System.out.println("\nПроверка IP: \"" + ip + "\"");

            // Базовая проверка формата (4 группы чисел, разделенных точками)
            // Регулярное выражение для IP-адреса:
            // ^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$
            // ^ - начало строки
            // (\d{1,3}) - группа из 1-3 цифр
            // \. - точка (экранирована)
            // $ - конец строки
            Pattern basicPattern = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
            Matcher matcher = basicPattern.matcher(ip);

            if (!matcher.matches()) {
                System.out.println("❌ НЕВЕРНО: некорректный формат IP-адреса");
                System.out.println("   Ожидается формат: XXX.XXX.XXX.XXX (где X - цифра)");
                return;
            }

            // Проверка диапазона каждого октета (0-255)
            for (int i = 1; i <= 4; i++) {
                String octet = matcher.group(i);

                // Проверка на ведущие нули (кроме "0")
                if (octet.length() > 1 && octet.startsWith("0")) {
                    System.out.println("❌ НЕВЕРНО: октет \"" + octet + "\" содержит ведущий ноль");
                    System.out.println("   IP-адреса не должны содержать ведущие нули");
                    return;
                }

                int value = Integer.parseInt(octet);
                if (value < 0 || value > 255) {
                    System.out.println("❌ НЕВЕРНО: октет \"" + octet + "\" вне диапазона [0-255]");
                    System.out.println("   Значение: " + value);
                    return;
                }
            }

            // Все проверки пройдены
            System.out.println("✅ ВЕРНО: IP-адрес корректен");
            System.out.println("   Октеты: " + matcher.group(1) + ", " +
                             matcher.group(2) + ", " +
                             matcher.group(3) + ", " +
                             matcher.group(4));

        } catch (PatternSyntaxException e) {
            System.err.println("Ошибка в регулярном выражении: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Ошибка преобразования числа: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка при проверке IP-адреса: " + e.getMessage());
        }
    }

    /**
     * Альтернативная версия с более сложным регулярным выражением
     * Проверяет IP-адрес одним регулярным выражением
     * @param ip IP-адрес для проверки
     * @return true если IP корректен
     */
    @SuppressWarnings("unused")
    private static boolean validateIPAdvanced(String ip) {
        try {
            // Сложное регулярное выражение, проверяющее диапазон 0-255 для каждого октета:
            // (25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?) - один октет:
            //   25[0-5] - 250-255
            //   2[0-4][0-9] - 200-249
            //   [01]?[0-9][0-9]? - 0-199
            String ipPattern =
                "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

            Pattern pattern = Pattern.compile(ipPattern);
            return pattern.matcher(ip).matches();

        } catch (Exception e) {
            System.err.println("Ошибка при проверке: " + e.getMessage());
            return false;
        }
    }
}
