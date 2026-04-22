package spring_lab3_notifications.org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import spring_lab3_notifications.org.example.model.dto.UserDto;
import spring_lab3_notifications.org.example.model.entity.User;
import spring_lab3_notifications.org.example.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/add")
    public UserDto createUser(@RequestBody @Valid UserDto request) { 
        User response = userService.createUser(request);
        return UserDto.builder()
            .name(response.getName())
            .email(response.getEmail())
            .phone(response.getPhone())
            .telegramChatId(response.getTelegramChatId())
            .deviceToken(response.getDeviceToken())
            .createdAt(response.getCreatedAt())
            .build();
    }

    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers().stream()
            .map(r -> UserDto.builder()
            .name(r.getName())
            .email(r.getEmail())
            .phone(r.getPhone())
            .telegramChatId(r.getTelegramChatId())
            .deviceToken(r.getDeviceToken())
            .createdAt(r.getCreatedAt())
            .build())
            .toList();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) { 
        User response = userService.getUserById(id); 
        return UserDto.builder()
            .name(response.getName())
            .email(response.getEmail())
            .phone(response.getPhone())
            .telegramChatId(response.getTelegramChatId())
            .deviceToken(response.getDeviceToken())
            .createdAt(response.getCreatedAt())
            .build();
    }
    
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody @Valid UserDto request) {
        User response = userService.updateUser(id, request); 
        return UserDto.builder()
            .name(response.getName())
            .email(response.getEmail())
            .phone(response.getPhone())
            .telegramChatId(response.getTelegramChatId())
            .deviceToken(response.getDeviceToken())
            .createdAt(response.getCreatedAt())
            .build();
    }
    
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return String.format("Пользователь %s удален", id);
    }
}