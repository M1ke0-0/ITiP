package lab6;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


class Product {
    String name;
    double price;

    Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

public class SalesTracker {
    private CopyOnWriteArrayList<Product> sales;

    public SalesTracker() {
        sales = new CopyOnWriteArrayList<>();
    }

    public void addSale(String name, double price) {
        sales.add(new Product(name, price));
    }

    public void showSales() {
        System.out.println("Проданные товары:");
        for (Product p : sales) {
            System.out.println(p.name + " - " + p.price + " руб.");
        }
    }

    public double getTotalSum() {
        double total = 0;
        for (Product p : sales) {
            total += p.price;
        }
        return total;
    }

    public String getMostPopular() {
        if (sales.isEmpty()) {
            return "Нет проданных товаров";
        }

        Map<String, Integer> productCount = new HashMap<>();
        for (Product p : sales) {
            productCount.put(p.name, productCount.getOrDefault(p.name, 0) + 1);
        }

        String popular = "";
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : productCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                popular = entry.getKey();
            }
        }
        return popular;
    }

    public static void main(String[] args) {
        SalesTracker tracker = new SalesTracker();

        tracker.addSale("Хлеб", 50.0);
        tracker.addSale("Молоко", 80.0);
        tracker.addSale("Хлеб", 50.0);
        tracker.addSale("Масло", 150.0);
        tracker.addSale("Молоко", 80.0);
        tracker.addSale("Хлеб", 50.0);
        tracker.showSales();

        System.out.println("\nОбщая сумма продаж: " + tracker.getTotalSum() + " руб.");
        System.out.println("Самый популярный товар: " + tracker.getMostPopular());
    }
}
