package spring_lab3_notifications.org.example.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class NotificationManager {
    
    private final Map<String, MessageService> services;
    private final MessageService defaultService;

    // Внедряем Карту всех сервисов и один конкретный через @Qualifier
    public NotificationManager(Map<String, MessageService> services, 
                               @Qualifier("customEmail") MessageService defaultService) {
        this.services = services;
        this.defaultService = defaultService;
    }

    public void notify(String message, String recipient, String type) {
        if (type == null || type.isEmpty()) {
            System.out.println("Using default @Qualifier service (customEmail):");
            defaultService.sendMessage(message, recipient);
        } else {
            MessageService service = services.get(type);
            if (service != null) {
                System.out.println("Using requested service: " + type);
                service.sendMessage(message, recipient);
            } else {
                System.out.println("Error: Service '" + type + "' not found in context!");
                System.out.println("Available services: " + services.keySet());
            }
        }
    }
    
    // Старый метод для совместимости (если нужно)
    public void notify(String message, String recipient) {
        this.notify(message, recipient, null);
    }
}