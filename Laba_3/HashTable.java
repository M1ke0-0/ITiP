package lab3;

import java.util.LinkedList;

public class HashTable {
    private LinkedList[] table;
    private int size;

    public HashTable(int capacity) {
        table = new LinkedList[capacity];
        size = 0;
    }

    private int hash(Object key) {
        return Math.abs(key.hashCode() % table.length);
    }

    public void put(Object key, Object value) {
        int index = hash(key);
        if (table[index] == null) {
            table[index] = new LinkedList();
        }

        for (Object obj : table[index]) {
            Entry entry = (Entry) obj;
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                return;
            }
        }

        table[index].add(new Entry(key, value));
        size++;
    }

    public Object get(Object key) {
        int index = hash(key);
        if (table[index] != null) {
            for (Object obj : table[index]) {
                Entry entry = (Entry) obj;
                if (entry.getKey().equals(key)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    public void remove(Object key) {
        int index = hash(key);
        if (table[index] != null) {
            for (Object obj : table[index]) {
                Entry entry = (Entry) obj;
                if (entry.getKey().equals(key)) {
                    table[index].remove(entry);
                    size--;
                    return;
                }
            }
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printTable() {
        for (int i = 0; i < table.length; i++) {
            System.out.print("Index " + i + ": ");
            if (table[i] != null) {
                for (Object obj : table[i]) {
                    System.out.print(obj + " -> ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        HashTable table = new HashTable(7);
        table.put("apple", 5);
        table.put("banana", 3);
        table.put("orange", 7);
        table.put("pear", 2);

        System.out.println("Содержимое таблицы:");
        table.printTable();

        System.out.println("\nЗначение по ключу 'banana': " + table.get("banana"));
        System.out.println("Удаляем 'banana'");
        table.remove("banana");
        System.out.println("Размер таблицы: " + table.size());

        System.out.println("\nПосле удаления:");
        table.printTable();
    }
}
