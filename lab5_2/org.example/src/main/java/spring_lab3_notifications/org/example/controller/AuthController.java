package spring_lab3_notifications.org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import spring_lab3_notifications.org.example.model.dto.LoginRequest;
import spring_lab3_notifications.org.example.model.dto.RegisterRequest;
import spring_lab3_notifications.org.example.security.JwtService;
import spring_lab3_notifications.org.example.service.AuthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // Регистрация обычного пользователя — открыта для всех
    @PostMapping("/register")
    public String register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return "Пользователь успешно зарегистрирован";
    }

    // Регистрация администратора — только для уже аутентифицированных ADMIN
    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String registerAdmin(@RequestBody @Valid RegisterRequest request) {
        authService.registerAdmin(request);
        return "Администратор успешно зарегистрирован";
    }

    // Логин — возвращает JWT токен; BadCredentialsException обрабатывается в GlobalExceptionHandler
    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        return jwtService.generateToken(request.getEmail());
    }
}
