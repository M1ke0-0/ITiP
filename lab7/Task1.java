package lab7;

import java.util.concurrent.atomic.AtomicLong;

// Реализация многопоточной программы для вычисления сум- мы элементов массива.
// Создать два потока, которые будут вычислять сумму элементов массива по половинкам,
// после чего результаты будут складываться в главном потоке.

public class Task1 {
    private static int[] array;
    private static AtomicLong sum1 = new AtomicLong(0);
    private static AtomicLong sum2 = new AtomicLong(0);

    public static void main(String[] args) {
        array = new int[1000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }

        Thread thread1 = new Thread(new SumCalculator(0, array.length / 2, sum1, 1));
        Thread thread2 = new Thread(new SumCalculator(array.length / 2, array.length, sum2, 2));

        long startTime = System.currentTimeMillis();

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        long totalSum = sum1.get() + sum2.get();

        System.out.println("Сумма первой половины (поток 1): " + sum1.get());
        System.out.println("Сумма второй половины (поток 2): " + sum2.get());
        System.out.println("Общая сумма: " + totalSum);
        System.out.println("Время выполнения: " + (endTime - startTime) + " мс");
    }

    static class SumCalculator implements Runnable {
        private int start;
        private int end;
        private AtomicLong sumReference;
        private int threadNumber;

        public SumCalculator(int start, int end, AtomicLong sumReference, int threadNumber) {
            this.start = start;
            this.end = end;
            this.sumReference = sumReference;
            this.threadNumber = threadNumber;
        }

        @Override
        public void run() {
            long partialSum = 0;
            System.out.println("Поток " + threadNumber + " начал работу (индексы " + start + "-" + (end - 1) + ")");

            for (int i = start; i < end; i++) {
                partialSum += array[i];
            }

            sumReference.set(partialSum);
            System.out.println("Поток " + threadNumber + " завершил работу. Сумма = " + partialSum);
        }
    }
}