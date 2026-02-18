package org.example;

public class Main {
    public static void main(String[] args) {
        Animal cat = new Cat("Кот 1", 3);
        Animal parrot = new Parrot("Попугай 1", 2);
        Animal fish = new Fish("Рыба 1", 1);

        System.out.println("Кот: " + cat.getName() + ", возраст: " + cat.getAge());
        cat.speak();
        cat.move();
        System.out.println("Попугай: " + parrot.getName() + ", возраст: " + parrot.getAge());
        parrot.speak();
        parrot.move();

        System.out.println("Рыбка: " + fish.getName() + ", возраст: " + fish.getAge());
        fish.speak();
        fish.move();
    }
}
