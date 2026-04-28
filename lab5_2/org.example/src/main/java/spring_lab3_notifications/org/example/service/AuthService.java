package spring_lab3_notifications.org.example.service;

import lombok.RequiredArgsConstructor;
import spring_lab3_notifications.org.example.exception.EmailAlreadyExistsException;
import spring_lab3_notifications.org.example.model.dto.RegisterRequest;
import spring_lab3_notifications.org.example.model.entity.User;
import spring_lab3_notifications.org.example.model.enums.UserRole;
import spring_lab3_notifications.org.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        registerWithRole(request, UserRole.ROLE_USER);
    }

    public void registerAdmin(RegisterRequest request) {
        registerWithRole(request, UserRole.ROLE_ADMIN);
    }

    private void registerWithRole(RegisterRequest request, UserRole role) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
