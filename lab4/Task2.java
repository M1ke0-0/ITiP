package lab4;

import java.io.*;

public class Task2 {
    public static void main(String[] args) {
        String sourceFile = "source.txt";
        String destFile = "destination.txt";
        String mockInvalidDestFile = "destinationdir.txt";

        // Создаем исходный файл для демонстрации
        createSourceFile(sourceFile);

        // Вариант 1: обработка ошибок открытия и закрытия файлов
        // copyFileVariant1(sourceFile, destFile);
        // Ошибка открытия файла (неверное название файла)
        // copyFileVariant1("nonexistent.txt", destFile);

        // Ошибка закрытия файла (укажем директорию вместо файла)
        // copyFileVariant1(sourceFile, mockInvalidDestFile);

        // Вариант 2: обработка ошибок чтения и записи файлов
        copyFileVariant2(sourceFile, destFile + ".v2");
    }

    // Вариант 1: Обработка ошибок открытия и закрытия файлов
    public static void copyFileVariant1(String source, String dest) {
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            System.out.println("\n=== Вариант 1: Обработка открытия/закрытия файлов ===");

            // Попытка открыть файлы
            File file = new File(source);
            fis = new FileInputStream(source);
            fos = new FileOutputStream(dest);
            int content;
            while ((content = fis.read()) != -1) {
                fos.write(content);
                file.delete();
            }

            System.out.println("Файл успешно скопирован из " + source + " в " + dest);

        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: файл не найден!");
            System.out.println("Детали: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода при работе с файлом!");
            System.out.println("Детали: " + e.getMessage());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                    System.out.println("Входной поток закрыт");
                }
                if (fos != null) {
                    fos.close();
                    System.out.println("Выходной поток закрыт");
                }
            } catch (IOException e) {
                System.out.println("Ошибка при закрытии файлов!");
                System.out.println("Детали: " + e.getMessage());
            }
        }
    }

    public static void copyFileVariant2(String source, String dest) {
        System.out.println("\n=== Вариант 2: Обработка чтения/записи файлов ===");

        try (
                FileInputStream fis = new FileInputStream(source);
                FileOutputStream fos = new FileOutputStream(dest)) {

            byte[] buffer = new byte[32];
            int length;

            try {
                while ((length = fis.read(buffer)) > 0) {
                    try {
                        System.out.println("Iter" + length);
                        Thread.sleep(1);
                        if (length != buffer.length) {{
                            throw new IOException("Симуляция ошибки во время чтения/записи");
                        }}
                        fos.write(buffer, 0, length);
                    } catch (IOException e) {
                        System.out.println("Ошибка при записи в файл!");
                        System.out.println("Детали: " + e.getMessage());
                        throw e; // Пробрасываем исключение дальше
                    }
                }
                System.out.println("Файл успешно скопирован из " + source + " в " + dest);

            } catch (IOException e) {
                System.out.println("Ошибка при чтении из файла!");
                System.out.println("Детали: " + e.getMessage());
            // }
            } catch (InterruptedException e) {
                System.out.println("Операция была прервана!");
                System.out.println("Детали: " + e.getMessage());
            }
            fos.write(buffer, 32, 32);
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: файл не найден!");
            System.out.println("Детали: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Общая ошибка ввода-вывода!");
            System.out.println("Детали: " + e.getMessage());
        }
    }

    private static void createSourceFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Это тестовое содержимое файла.\n");
            writer.write("Строка 2: Java Exception Handling.\n");
            writer.write("Строка 3: Лабораторная работа №4.\n");
            System.out.println("Исходный файл " + filename + " создан.");
        } catch (IOException e) {
            System.out.println("Ошибка при создании исходного файла: " + e.getMessage());
        }
    }
}