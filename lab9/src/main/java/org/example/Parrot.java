package org.example;

public class Parrot extends Animal {
    public Parrot(String name, int age) {
        super(name, age);
    }

    @Override
    public void speak() {
        System.out.println(getName() + " говорит: Привет! Карр!");
    }

    @Override
    public void move() {
        System.out.println(getName() + " летает.");
    }
}
