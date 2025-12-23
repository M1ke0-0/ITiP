package lab7;

import java.util.Random;

// Реализация многопоточной программы для поиска наиболь- шего элемента в матрице.
// Создать несколько потоков, каждый из которых будет обрабатывать свою строку матрицы.
// После завершения работы всех потоков результаты будут сравниваться в главном потоке для нахождения наибольшего элемента.

public class Task2 {
    private static int[][] matrix;
    private static int[] rowMaxValues;
    private static final int ROWS = 5;
    private static final int COLS = 10;

    public static void main(String[] args) {

        matrix = new int[ROWS][COLS];
        rowMaxValues = new int[ROWS];
        Random random = new Random();

        System.out.println("Матрица " + ROWS + "x" + COLS + ":");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                matrix[i][j] = random.nextInt(100);
                System.out.printf("%4d", matrix[i][j]);
            }
            System.out.println();
        }

        Thread[] threads = new Thread[ROWS];
        for (int i = 0; i < ROWS; i++) {
            threads[i] = new Thread(new MaxFinder(i));
        }

        long startTime = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.start();
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        int globalMax = Integer.MIN_VALUE;
        int maxRow = -1;

        for (int i = 0; i < ROWS; i++) {
            System.out.println("Строка " + i + ": максимальный элемент = " + rowMaxValues[i]);
            if (rowMaxValues[i] > globalMax) {
                globalMax = rowMaxValues[i];
                maxRow = i;
            }
        }

        System.out.println("Наибольший элемент в матрице: " + globalMax);
        System.out.println("Найден в строке: " + maxRow);
        System.out.println("Время выполнения: " + (endTime - startTime) + " мс");
    }

    static class MaxFinder implements Runnable {
        private int rowIndex;

        public MaxFinder(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        @Override
        public void run() {
            System.out.println("Поток для строки " + rowIndex + " начал работу");

            int max = Integer.MIN_VALUE;
            for (int j = 0; j < COLS; j++) {
                if (matrix[rowIndex][j] > max) {
                    max = matrix[rowIndex][j];
                }
            }

            rowMaxValues[rowIndex] = max;
            System.out.println("Поток для строки " + rowIndex + " завершил работу. Максимум = " + max);
        }
    }
}