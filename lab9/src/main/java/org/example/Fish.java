package org.example;

public class Fish extends Animal {
    public Fish(String name, int age) {
        super(name, age);
    }

    @Override
    public void speak() {
        System.out.println(getName() + " молчит...");
    }

    @Override
    public void move() {
        System.out.println(getName() + " плавает.");
    }
}
