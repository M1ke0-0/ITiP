package spring_lab3_notifications.org.example.service;

import org.springframework.stereotype.Service;

@Service
public class PushService implements MessageService {
    
    @Override
    public void sendMessage(String message, String recipient) {
        System.out.println("PushService working");
        System.out.println("Уведомление: " + message + " токен: " + recipient);
    }
}
