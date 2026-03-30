package org.example.service;

import org.springframework.stereotype.Service;

// Самостоятельное задание Части 5, п.2: кастомное имя бина
@Service("customEmail")
public class EmailService implements MessageService {
    @Override
    public void sendMessage(String message, String recipient) {
        System.out.println("EMAIL to " + recipient + ": " + message);
    }
}
