package org.example.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// Часть 4: только @ComponentScan, без явных @Bean методов
@Configuration
@ComponentScan("org.example")
public class AppConfig {
    // Никаких @Bean методов - Spring сам находит компоненты по аннотациям
}
