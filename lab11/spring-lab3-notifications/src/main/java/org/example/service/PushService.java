package org.example.service;

import org.springframework.stereotype.Service;

// Самостоятельное задание Части 2, п.2 и Части 3, п.2
@Service
public class PushService implements MessageService {
    @Override
    public void sendMessage(String message, String recipient) {
        System.out.println("PUSH to " + recipient + ": " + message);
    }
}
