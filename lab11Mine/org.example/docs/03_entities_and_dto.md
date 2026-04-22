# Сущности и DTO

## Сущности (Entity)

### User — таблица `users`
| Поле | Тип | Описание |
|---|---|---|
| `id` | Long | Первичный ключ, автогенерация |
| `name` | String | Имя пользователя (NOT NULL) |
| `email` | String | Email |
| `phone` | String | Телефон |
| `deviceToken` | String | Токен для push-уведомлений |
| `telegramChatId` | String | ID чата Telegram |
| `createdAt` | LocalDateTime | Дата создания |
| `notifications` | List<Notification> | Список уведомлений (OneToMany) |

### Notification — таблица `notifications`
| Поле | Тип | Описание |
|---|---|---|
| `id` | Long | Первичный ключ, автогенерация |
| `title` | String | Заголовок (NOT NULL) |
| `message` | String | Текст (TEXT, NOT NULL) |
| `channel` | NotificationChannel | Канал отправки (enum) |
| `status` | NotificationStatus | Статус (enum) |
| `createdAt` | LocalDateTime | Дата создания |
| `sentAt` | LocalDateTime | Дата отправки |
| `recipient` | User | Получатель (ManyToOne, FK: recipient_id) |

### Связь между таблицами
```
users (1) ←——→ (N) notifications
                    recipient_id → users.id
```

---

## DTO (Data Transfer Object)

DTO — объекты для передачи данных между клиентом и сервером. Скрывают внутренние детали сущностей.

### UserDto
- Не содержит `id` — клиент не задаёт ID вручную
- Валидация: `name` обязателен, `email` проверяется по формату и паттерну, `phone` по регулярному выражению

### NotificationDto
- Вместо объекта `User` содержит только `recipientId` (Long)
- Валидация: `title`, `message` обязательны, `channel` и `recipientId` не могут быть null

---

## Enums

### NotificationChannel
```java
EMAIL, SMS, PUSH, TELEGRAM
```

### NotificationStatus
```java
CREATED, SENT, FAILED
```

Хранятся в БД как строки (`@Enumerated(EnumType.STRING)`), а не числа — для читаемости.
