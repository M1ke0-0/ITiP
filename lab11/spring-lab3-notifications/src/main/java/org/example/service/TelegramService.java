package org.example.service;

import org.springframework.stereotype.Service;

// Самостоятельное задание Части 5, п.3: четвертый сервис
@Service
public class TelegramService implements AdvancedMessageService {
    @Override
    public void sendMessage(String message, String recipient) {
        System.out.println("TELEGRAM to " + recipient + ": " + message);
    }

    @Override
    public String getServiceType() {
        return "Telegram";
    }
}
