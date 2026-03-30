package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

// Финальная версия: Часть 5, Способ 3 + дополнительное задание (Map)
@Service
public class NotificationManager {
    private final List<MessageService> messageServices;
    private final Map<String, MessageService> messageServiceMap;

    @Autowired
    public NotificationManager(List<MessageService> messageServices,
                                Map<String, MessageService> messageServiceMap) {
        this.messageServices = messageServices;
        this.messageServiceMap = messageServiceMap;
    }

    // Отправить через все сервисы
    public void notify(String message, String recipient) {
        messageServices.forEach(service ->
                service.sendMessage(message, recipient));
    }

    // Отправить через конкретный сервис по имени бина (доп. задание Части 5)
    public void notifyVia(String serviceName, String message, String recipient) {
        MessageService service = messageServiceMap.get(serviceName);
        if (service != null) {
            service.sendMessage(message, recipient);
        } else {
            System.out.println("Сервис '" + serviceName + "' не найден. Доступные: " + messageServiceMap.keySet());
        }
    }
}
