package org.example.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

// Самостоятельное задание Части 5, п.1: @Primary на SmsService
@Service
@Primary
public class SmsService implements MessageService {
    @Override
    public void sendMessage(String message, String recipient) {
        System.out.println("SMS to " + recipient + ": " + message);
    }
}
