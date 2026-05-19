package spring_lab3_notifications.org.example.service;

import spring_lab3_notifications.org.example.model.dto.NotificationDto;
import spring_lab3_notifications.org.example.model.entity.Notification;
import spring_lab3_notifications.org.example.model.entity.User;
import spring_lab3_notifications.org.example.model.enums.NotificationChannel;
import spring_lab3_notifications.org.example.model.enums.NotificationStatus;
import spring_lab3_notifications.org.example.repository.NotificationRepository;
import spring_lab3_notifications.org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldCreateNotification() {
        User user = new User();
        user.setId(1L);
        user.setEmail("ivan@example.com");

        NotificationDto dto = NotificationDto.builder()
                .title("Напоминание")
                .message("Завтра пара по Spring")
                .channel(NotificationChannel.EMAIL)
                .recipientId(1L)
                .build();

        Notification savedNotification = new Notification();
        savedNotification.setId(10L);
        savedNotification.setTitle(dto.getTitle());
        savedNotification.setMessage(dto.getMessage());
        savedNotification.setChannel(dto.getChannel());
        savedNotification.setRecipient(user);
        savedNotification.setStatus(NotificationStatus.CREATED);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        Notification result = notificationService.createNotification(dto);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Напоминание", result.getTitle());
        assertEquals(NotificationChannel.EMAIL, result.getChannel());
        assertEquals(user, result.getRecipient());

        verify(userRepository, times(1)).findById(1L);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void shouldGetNotificationById() {
        Notification notification = new Notification();
        notification.setId(10L);
        notification.setTitle("Важно");
        notification.setMessage("Сообщение");

        when(notificationRepository.findById(10L)).thenReturn(Optional.of(notification));

        Notification result = notificationService.getNotificationById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Важно", result.getTitle());

        verify(notificationRepository, times(1)).findById(10L);
    }

    @Test
    void shouldThrowExceptionWhenNotificationNotFound() {
        when(notificationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notificationService.getNotificationById(999L));

        verify(notificationRepository, times(1)).findById(999L);
    }
}
