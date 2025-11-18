package lab4;

public class Task1 {
    public static void main(String[] args) {
        String[] arr = {"1", "2", "3", "4", "5"};
        int sum = 0;
        
        try {
            // for (int i = 0; i < arr.length; i++) {
            for (int i = 0; i <= arr.length; i++) {
                sum += Integer.parseInt(arr[i]);
            }

            double average = (double) sum / arr.length;
            System.out.println("Среднее арифметическое: " + average);

            
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка: выход за границы массив");
            System.out.println("Сообщение исключения: " + e.getMessage());
            
            // // Вычисляем корректное среднее
            // sum = 0;
            // for (int i = 0; i < arr.length; i++) {
            //     sum += arr[i];
            // }
            // double average = (double) sum / arr.length;
            // System.out.println("Корректное среднее арифметическое: " + average);
            
        } catch (NumberFormatException e) {
            // Обработка неверных данных
            System.out.println("элемент массива не является числом");
            System.out.println("Сообщение исключения: " + e.getMessage());
        }
        
        System.out.println("\nСодержимое массива:");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}