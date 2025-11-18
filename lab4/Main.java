package lab4;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            displayMenu();
            
            try {
                System.out.print("\nВыберите пункт меню (1-4): ");
                String choice = scanner.nextLine().trim();
                
                switch (choice) {
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
                        System.out.println("\nЗавершение работы");
                        running = false;
                        break;
                    default:
                        System.out.println("\n Неверный выбор/ выберите пункт от 1 до 4.");
                }
                
                if (running && !choice.equals("4")) {
                    System.out.println("\n" + "=".repeat(60));
                    System.out.print("Нажмите Enter ");
                    scanner.nextLine();
                    clearScreen();
                }
                
            } catch (Exception e) {
                System.out.println("\n ошибка: " + e.getMessage());
                ExceptionLogger.logException(e, "Ошибка в главном меню");
            }
        }
        
        scanner.close();
    }
    
    private static void displayMenu() {

        System.out.println("1. Задание 1 - Среднее арифметическое массива");
        System.out.println("2. Задание 2 - Копирование файлов");
        System.out.println("3. Задание 3 (Вариант 6) - Проверка ввода числа");
        System.out.println(" 4. Выход");
    }
    
    private static void runTask1() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("ЗАДАНИЕ 1: СРЕДНЕЕ АРИФМЕТИЧЕСКОЕ МАССИВА");
        System.out.println("═".repeat(60));
        
        Task1.main(new String[]{});
    }
    
    private static void runTask2() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("ЗАДАНИЕ 2: КОПИРОВАНИЕ ФАЙЛОВ");
        System.out.println("═".repeat(60));
        
        Task2.main(new String[]{});
    }
    
    private static void runTask3() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("ЗАДАНИЕ 3 (ВАРИАНТ 6): ПРОВЕРКА ВВОДА ЦЕЛОГО ЧИСЛА");
        System.out.println("═".repeat(60));
        
        Task3_Variant6.main(new String[]{});
    }
    
    private static void clearScreen() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                for (int i = 0; i < 2; i++) {
                    System.out.println();
                }
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 2; i++) {
                System.out.println();
            }
        }
    }
}