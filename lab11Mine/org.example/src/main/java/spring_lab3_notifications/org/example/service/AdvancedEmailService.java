package spring_lab3_notifications.org.example.service;

import org.springframework.stereotype.Service;

@Service
public class AdvancedEmailService implements AdvancedMessageService {

    @Override
    public void sendMessage(String message, String recipient) {
        System.out.println("Advanced EMAIL to " + recipient + ": " + message);
    }

    @Override
    public String getServiceType() {
        return "Email Service (Advanced)";
    }
}
