package org.example.controller;

import org.example.service.NotificationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private final NotificationManager notificationManager;

    public NotificationController(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    // Отправить через все доступные сервисы
    @GetMapping("/notify")
    public String notify(@RequestParam String message, @RequestParam String email) {
        notificationManager.notify(message, email);
        return "Уведомление отправлено через все сервисы (аннотации)";
    }

    // Дополнительное задание Части 5: отправить через конкретный сервис по имени
    @GetMapping("/notify-via")
    public String notifyVia(@RequestParam String service,
                             @RequestParam String message,
                             @RequestParam String recipient) {
        notificationManager.notifyVia(service, message, recipient);
        return "Уведомление отправлено через сервис: " + service;
    }
}
