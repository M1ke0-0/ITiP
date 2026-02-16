package lab3;

import java.util.HashMap;

public class Warehouse {
    private HashMap storage;

    public Warehouse() {
        storage = new HashMap();
    }

    public void addProduct(String barcode, Product product) {
        storage.put(barcode, product);
    }

    public Product findProduct(String barcode) {
        return (Product) storage.get(barcode);
    }

    public void removeProduct(String barcode) {
        storage.remove(barcode);
    }

    public void printAllProducts() {
        for (Object key : storage.keySet()) {
            System.out.println("Штрихкод: " + key + " -> " + storage.get(key));
        }
    }

    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();

        warehouse.addProduct("111", new Product("Яблоко", 1.5, 100));
        warehouse.addProduct("222", new Product("Банан", 2.0, 50));
        warehouse.addProduct("333", new Product("Апельсин", 3.0, 80));

        System.out.println("Все товары на складе:");
        warehouse.printAllProducts();

        System.out.println("\nПоиск товара с кодом 222:");
        System.out.println(warehouse.findProduct("222"));

        System.out.println("\nУдаление товара с кодом 111...");
        warehouse.removeProduct("111");

        System.out.println("\nПосле удаления:");
        warehouse.printAllProducts();
    }
}
