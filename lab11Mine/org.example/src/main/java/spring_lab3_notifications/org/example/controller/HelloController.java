package spring_lab3_notifications.org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        System.out.println("/hello working");
        return "Привет, Spring Boot!";
    }

    @GetMapping("/goodbye")
    public String sayGoodbye() {
        System.out.println("/goodbye working");
        return "До свидания, Spring Boot!";
    }

    @GetMapping("/hello/{name}")
    public String sayHelloName(@PathVariable String name) {
        System.out.println("/hello/{name} working");
        return "Привет, " + name + "!";
    }

    @GetMapping("hello/{name}/{age}")
    public String sayHelloNameAge(@PathVariable String name, @PathVariable String age){
        System.out.println("/hello/{name}/{age} working");
        return "Привет " + name + "!" + "Твой возраст: " + age;
    }
}
