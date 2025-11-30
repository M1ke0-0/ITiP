package lab5;

import java.util.Scanner;

/**
 * Демонстрационный класс для лабораторной работы №5
 * "Строки и регулярные выражения"
 *
 * Содержит меню для запуска всех заданий лабораторной работы.
 */
public class Lab5Demo {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printMenu();
                System.out.print("Выберите задание (1-5) или 0 для выхода: ");

                String input = scanner.nextLine().trim();

                if (input.equals("0")) {
                    System.out.println("\nВыход из программы. До свидания!");
                    break;
                }

                System.out.println("\n" + "=".repeat(70) + "\n");

                switch (input) {
                    case "1":
                        runTask1();
                        break;
                    case "2":
                        runTask2();
                        break;
                    case "3":
                        runTask3();
                        break;
                    case "4":
                        runTask4();
                        break;
                    case "5":
                        runTask5();
                        break;
                    default:
                        System.out.println("❌ Неверный выбор! Пожалуйста, выберите число от 0 до 5.");
                }

                System.out.println("\n" + "=".repeat(70) + "\n");
                System.out.println("Нажмите Enter для возврата в меню...");
                scanner.nextLine();
            }
        } catch (Exception e) {
            System.err.println("Критическая ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Выводит главное меню программы
     */
    private static void printMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("     ЛАБОРАТОРНАЯ РАБОТА №5: СТРОКИ И РЕГУЛЯРНЫЕ ВЫРАЖЕНИЯ");
        System.out.println("=".repeat(70));
        System.out.println();
        System.out.println("  1. Задание 1: Поиск всех чисел в тексте");
        System.out.println("     └─ Поиск целых и дробных чисел с использованием регулярных выражений");
        System.out.println();
        System.out.println("  2. Задание 2: Проверка корректности ввода пароля");
        System.out.println("     └─ Валидация пароля (8-16 символов, заглавные буквы, цифры)");
        System.out.println();
        System.out.println("  3. Задание 3: Поиск заглавной буквы после строчной");
        System.out.println("     └─ Выделение переходов строчная→ЗАГЛАВНАЯ символами '!'");
        System.out.println();
        System.out.println("  4. Задание 4: Проверка корректности ввода IP-адреса");
        System.out.println("     └─ Валидация IPv4 адресов (формат XXX.XXX.XXX.XXX, 0-255)");
        System.out.println();
        System.out.println("  5. Задание 5: Поиск слов по начальной букве");
        System.out.println("     └─ Поиск всех слов, начинающихся с заданной буквы");
        System.out.println();
        System.out.println("  0. Выход");
        System.out.println();
        System.out.println("=".repeat(70));
    }

    /**
     * Запускает задание 1
     */
    private static void runTask1() {
        try {
            Task1.main(new String[]{});
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении задания 1: " + e.getMessage());
        }
    }

    /**
     * Запускает задание 2
     */
    private static void runTask2() {
        try {
            Task2.main(new String[]{});
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении задания 2: " + e.getMessage());
        }
    }

    /**
     * Запускает задание 3
     */
    private static void runTask3() {
        try {
            Task3.main(new String[]{});
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении задания 3: " + e.getMessage());
        }
    }

    /**
     * Запускает задание 4
     */
    private static void runTask4() {
        try {
            Task4.main(new String[]{});
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении задания 4: " + e.getMessage());
        }
    }

    /**
     * Запускает задание 5
     */
    private static void runTask5() {
        try {
            Task5.main(new String[]{});
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении задания 5: " + e.getMessage());
        }
    }
}
