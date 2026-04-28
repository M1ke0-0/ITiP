package spring_lab3_notifications.org.example.model.mapper;

import org.springframework.stereotype.Component;
import spring_lab3_notifications.org.example.model.dto.UserDto;
import spring_lab3_notifications.org.example.model.entity.User;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .deviceToken(user.getDeviceToken())
                .telegramChatId(user.getTelegramChatId())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
