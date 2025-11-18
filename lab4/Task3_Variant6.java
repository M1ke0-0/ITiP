package lab4;

import java.util.Scanner;

public class Task3_Variant6 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Программа проверки ввода целого числа ===");
        System.out.println("Введите целое число:");
        
        try {
            String input = scanner.nextLine();
            int number = readInteger(input);
            System.out.println("Успешно! Вы ввели число: " + number);
            System.out.println("Квадрат числа: " + (number * number));
            
        } catch (CustomInputMismatchException e) {
            System.err.println("\n❌ " + e);
            ExceptionLogger.logException(e, "Пользователь ввел некорректные данные");
            System.out.println("\nПопробуйте еще раз с корректным целым числом.");
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Демонстрация различных случаев:");
        System.out.println("=".repeat(50));
        
        testInput("123");
        testInput("abc");
        testInput("12.5");
        testInput("");
        testInput("  456  ");
        testInput("999999999999999999");
        
        scanner.close();
    }
    
    public static int readInteger(String input) throws CustomInputMismatchException {
        if (input == null || input.trim().isEmpty()) {
            CustomInputMismatchException exception = 
                new CustomInputMismatchException(
                    "Ввод не может быть пустым!", 
                    input
                );
            ExceptionLogger.logException(exception);
            throw exception;
        }
        
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            CustomInputMismatchException exception = 
                new CustomInputMismatchException(
                    "Введенное значение не является целым числом!", 
                    input
                );
            ExceptionLogger.logException(exception);
            throw exception;
        }
    }
    
    private static void testInput(String input) {
        System.out.println("\nТест ввода: \"" + input + "\"");
        try {
            int number = readInteger(input);
            System.out.println("✓ Результат: " + number);
        } catch (CustomInputMismatchException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }
}