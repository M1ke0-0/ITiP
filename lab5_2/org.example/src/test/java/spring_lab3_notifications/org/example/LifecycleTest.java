package spring_lab3_notifications.org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LifecycleTest {
    @BeforeEach
    void setUp() {
        System.out.println("Подготовка перед каждым тестом");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Завершение после каждого теста");
    }

    @Test
    void firstTest() {
        System.out.println("Первый тест");
    }

    @Test
    void secondTest() {
        System.out.println("Второй тест");
    }
}
