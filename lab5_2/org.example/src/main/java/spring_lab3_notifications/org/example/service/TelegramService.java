package spring_lab3_notifications.org.example.service;

import org.springframework.stereotype.Service;

@Service
public class TelegramService implements MessageService {
    
    @Override
    public void sendMessage(String message, String recipient) {
        System.out.println("TelegramService working");
        System.out.println("TG to " + recipient + ": " + message );
    }
}
