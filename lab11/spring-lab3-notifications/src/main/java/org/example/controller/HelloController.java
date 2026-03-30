package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // Часть 1, базовое задание
    @GetMapping("/hello")
    public String sayHello() {
        return "Привет, Spring Boot!";
    }

    // Самостоятельное задание Части 1, п.1
    @GetMapping("/goodbye")
    public String sayGoodbye() {
        return "До свидания, Spring Boot!";
    }

    // Самостоятельное задание Части 1, п.2
    @GetMapping("/greet")
    public String greet(@RequestParam String name) {
        return "Привет, " + name + "!";
    }

    // Самостоятельное задание Части 1, п.3 (дополнительное)
    @GetMapping("/user-info")
    public String userInfo(@RequestParam String name, @RequestParam int age) {
        return "Пользователь: " + name + ", возраст: " + age + " лет";
    }
}
