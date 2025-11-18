package lab4;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExceptionLogger {
    private static final String LOG_FILE = "exceptions.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void logException(Exception e) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
                PrintWriter pw = new PrintWriter(fw)) {

            pw.println("=".repeat(60));
            pw.println("Время: " + LocalDateTime.now().format(formatter));
            pw.println("Тип исключения: " + e.getClass().getName());
            pw.println("Сообщение: " + e.getMessage());
            pw.println("Stack trace:");
            e.printStackTrace(pw);
            pw.println();

            System.out.println("Исключение записано в лог-файл: " + LOG_FILE);

        } catch (IOException ioException) {
            System.err.println("Ошибка при записи в лог-файл: " +
                    ioException.getMessage());
        }
    }

    public static void logException(Exception e, String additionalInfo) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
                PrintWriter pw = new PrintWriter(fw)) {

            pw.println("=".repeat(60));
            pw.println("Время: " + LocalDateTime.now().format(formatter));
            pw.println("Тип исключения: " + e.getClass().getName());
            pw.println("Сообщение: " + e.getMessage());
            pw.println("Дополнительная информация: " + additionalInfo);
            pw.println("Stack trace:");
            e.printStackTrace(pw);
            pw.println();

            System.out.println("Исключение записано в лог-файл: " + LOG_FILE);

        } catch (IOException ioException) {
            System.err.println("Ошибка при записи в лог-файл: " +
                    ioException.getMessage());
        }
    }
}