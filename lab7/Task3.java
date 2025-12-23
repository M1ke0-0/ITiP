package lab7;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

// У вас есть склад с товарами, которые нужно перенести на другой склад. У каждого товара есть свой вес.
// На складе работают 3 грузчика. Грузчики могут переносить товары одно- временно, но суммарный вес товаров,
// переносимый ими за одну итерацию, не может превышать 150 кг. Как только грузчики соберут 150 кг
// товаров, они отправятся на другой склад и начнут разгружать товары.

// 10. Использование Executor и CompletionService. Используй- те Executor для управления потоками и CompletionService для по-
// лучения результатов выполнения.


public class Task3 {
    private static final int MAX_WEIGHT_PER_TRIP = 150;
    private static final int NUM_WORKERS = 3;
    private static final int TOTAL_GOODS = 20;
    private static List<Integer> warehouse = new ArrayList<>();

    public static void main(String[] args) {

        Random random = new Random();
        System.out.println("Товары на складе:");
        for (int i = 0; i < TOTAL_GOODS; i++) {
            int weight = random.nextInt(50) + 10;
            warehouse.add(weight);
            System.out.println("Товар " + (i + 1) + ": " + weight + " кг");
        }

        int totalWeight = warehouse.stream().mapToInt(Integer::intValue).sum();
        System.out.println("\nОбщий вес товаров: " + totalWeight + " кг");
        System.out.println("Максимальный вес за одну поездку: " + MAX_WEIGHT_PER_TRIP + " кг");
        System.out.println("Количество грузчиков: " + NUM_WORKERS + "\n");

        ExecutorService executor = Executors.newFixedThreadPool(NUM_WORKERS);
        CompletionService<WorkerResult> completionService = new ExecutorCompletionService<>(executor);

        int tripNumber = 1;

        while (!warehouse.isEmpty()) {
            System.out.println("--- Поездка #" + tripNumber + " ---");

            List<Integer> currentLoad = new ArrayList<>();
            int currentWeight = 0;

            synchronized (warehouse) {
                while (!warehouse.isEmpty() && currentWeight < MAX_WEIGHT_PER_TRIP) {
                    int nextWeight = warehouse.get(0);
                    if (currentWeight + nextWeight <= MAX_WEIGHT_PER_TRIP) {
                        warehouse.remove(0);
                        currentLoad.add(nextWeight);
                        currentWeight += nextWeight;
                    } else {
                        break;
                    }
                }
            }

            if (currentLoad.isEmpty()) {
                synchronized (warehouse) {
                    if (!warehouse.isEmpty()) {
                        int heavyItem = warehouse.remove(0);
                        currentLoad.add(heavyItem);
                        currentWeight = heavyItem;
                    }
                }
            }

            System.out.println("Собрано товаров: " + currentLoad.size() + " шт., общий вес: " + currentWeight + " кг");

            int itemsPerWorker = currentLoad.size() / NUM_WORKERS;
            int remainingItems = currentLoad.size() % NUM_WORKERS;

            int startIndex = 0;
            for (int i = 0; i < NUM_WORKERS; i++) {
                int itemsForThisWorker = itemsPerWorker + (i < remainingItems ? 1 : 0);

                if (itemsForThisWorker > 0) {
                    List<Integer> workerLoad = currentLoad.subList(startIndex, startIndex + itemsForThisWorker);
                    completionService.submit(new Worker(i + 1, new ArrayList<>(workerLoad), tripNumber));
                    startIndex += itemsForThisWorker;
                }
            }

            int activeWorkers = Math.min(NUM_WORKERS, currentLoad.size());
            for (int i = 0; i < activeWorkers; i++) {
                try {
                    Future<WorkerResult> future = completionService.take();
                    WorkerResult result = future.get();
                    System.out.println("  -> " + result.getMessage());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Поездка #" + tripNumber + " завершена. Товары доставлены.\n");
            tripNumber++;
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("Все товары перенесены");
        System.out.println("Всего поездок: " + (tripNumber - 1));
    }

    static class Worker implements Callable<WorkerResult> {
        private int workerId;
        private List<Integer> items;

        public Worker(int workerId, List<Integer> items, int tripNumber) {
            this.workerId = workerId;
            this.items = items;
        }

        @Override
        public WorkerResult call() throws Exception {
            int totalWeight = items.stream().mapToInt(Integer::intValue).sum();

            Thread.sleep(totalWeight * 10);

            String message = "Грузчик " + workerId + " перенес " + items.size() +
                    " товар(ов) весом " + totalWeight + " кг";

            return new WorkerResult(workerId, totalWeight, message);
        }
    }

    static class WorkerResult {
        private int workerId;
        private int weight;
        private String message;

        public WorkerResult(int workerId, int weight, String message) {
            this.workerId = workerId;
            this.weight = weight;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public int getWorkerId() {
            return workerId;
        }

        public int getWeight() {
            return weight;
        }
    }
}