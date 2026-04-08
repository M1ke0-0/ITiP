package spring_lab3_notifications.org.example.controller;

import spring_lab3_notifications.org.example.service.NotificationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
    
    private final NotificationManager notificationManager;

    public NotificationController(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }
    
    @GetMapping("/notify")
    public String notify(@RequestParam String message, 
                         @RequestParam String recipient,
                         @RequestParam(required = false) String type) {
        
        notificationManager.notify(message, recipient, type);
        
        return "Уведомление отправлено (динамически)! Проверьте консоль.";
    }
}