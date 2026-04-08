package spring_lab3_notifications.org.example.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("spring_lab3_notifications.org.example") // Сканируем всё дерево проекта
public class AppConfig {
    // Никаких @Bean методов! Spring сам найдет всё, что помечено @Service/@RestController
}