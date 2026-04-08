package spring_lab3_notifications.org.example.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class SmsService implements MessageService {
    
    @Override
    public void sendMessage(String message, String recipient) {
        System.out.println("SmsService working");
        System.out.println("SMS to " + recipient + ": " + message );
    }
}