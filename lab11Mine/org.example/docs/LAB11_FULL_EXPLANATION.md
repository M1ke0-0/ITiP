# Полное объяснение Лабораторной работы 11 — Spring Data JPA

---

## Часть 1. Что такое Spring Data JPA

**JPA (Java Persistence API)** — стандарт Java для работы с реляционными БД через объекты.  
**Hibernate** — реализация JPA. Превращает Java-классы в таблицы БД и обратно.  
**Spring Data JPA** — надстройка над JPA, которая автоматически создаёт реализацию репозиториев.

Вместо написания SQL вручную мы описываем объекты (`@Entity`), а Hibernate сам строит запросы.

---

## Часть 2. Подключение к базе данных

Файл `application.properties`:
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

- `ddl-auto=update` — Hibernate автоматически создаёт/изменяет таблицы при запуске
- `show-sql=true` — все SQL-запросы выводятся в консоль
- Данные подключения берутся из переменных окружения (не хранятся в коде)

---

## Часть 3. Сущности (Entity)

Сущность — Java-класс, который соответствует таблице в БД.

### User
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          // PRIMARY KEY, автоинкремент

    @Column(nullable = false)
    private String name;      // NOT NULL

    private String email;
    private String phone;
    private String deviceToken;
    private String telegramChatId;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    private List<Notification> notifications; // один пользователь — много уведомлений
}
```

### Notification
```java
@Entity
@Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;   // TEXT — для длинных строк

    @Enumerated(EnumType.STRING)  // хранится как "EMAIL", не как 0
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime sentAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;   // внешний ключ на users.id
}
```

### Связь между таблицами
```
users                    notifications
------                   -------------
id (PK)   ←———————————  recipient_id (FK)
name                     title
email                    message
...                      channel
                         status
                         ...
```
- `@OneToMany` в User — у одного пользователя много уведомлений
- `@ManyToOne` в Notification — много уведомлений принадлежат одному пользователю
- `mappedBy = "recipient"` — говорит JPA что связь уже описана в поле `recipient` у Notification
- `FetchType.LAZY` — User загружается только при обращении, экономит запросы к БД

---

## Часть 4. DTO (Data Transfer Object)

**Зачем нужны DTO?** Сущности (`@Entity`) нельзя напрямую отдавать клиенту:
- Могут содержать чувствительные данные
- Имеют циклические ссылки (User → Notification → User → ...)
- Клиент не должен задавать `id` и `createdAt` вручную

**UserDto** — то что клиент отправляет/получает:
```java
public class UserDto {
    @NotBlank(message = "Имя не должно быть пустым")
    @Size(max = 100)
    private String name;

    @NotBlank @Email
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$")
    private String phone;
    // нет поля id — клиент не задаёт ID
}
```

**NotificationDto** — вместо объекта User передаётся только `recipientId`:
```java
public class NotificationDto {
    @NotBlank private String title;
    @NotBlank private String message;
    @NotNull  private NotificationChannel channel;
    private NotificationStatus status;
    @NotNull  private Long recipientId;  // только ID, не весь объект
}
```

---

## Часть 5. Репозитории

Репозиторий — интерфейс для работы с БД. Spring Data JPA автоматически создаёт реализацию.

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
```

`JpaRepository<User, Long>` даёт готовые методы:
- `save(entity)` — сохранить/обновить
- `findById(id)` — найти по ID → возвращает `Optional`
- `findAll()` — все записи
- `delete(entity)` — удалить
- `count()` — количество записей

### Query Derivation — генерация запросов по имени метода
```java
// WHERE status = ?
List<Notification> findByStatus(NotificationStatus status);

// WHERE status = ? AND channel = ?
List<Notification> findByStatusAndChannel(NotificationStatus status, NotificationChannel channel);

// WHERE status = ? ORDER BY created_at ASC
List<Notification> findByStatusOrderByCreatedAtAsc(NotificationStatus status);

// ORDER BY created_at DESC
List<Notification> findAllByOrderByCreatedAtDesc();
```
Spring читает имя метода и строит SQL — никакого кода писать не нужно.

### @Query — ручные запросы

**JPQL** (работает с Java-объектами и их полями):
```java
@Query("select n from Notification n where n.recipient.id = :recipientId and n.status = :status")
List<Notification> findByRecipientIdAndStatus(@Param("recipientId") Long recipientId,
                                              @Param("status") NotificationStatus status);
```

**Native SQL** (работает с именами таблиц и колонок):
```java
@Query(value = "select * from notifications where status = :status and channel = :channel",
       nativeQuery = true)
List<Notification> findNativeByStatusAndChannel(@Param("status") String status,
                                                @Param("channel") String channel);
```

Отличие: JPQL не зависит от структуры БД (можно переименовать таблицу), Native SQL — быстрее и позволяет использовать специфичные функции БД.

---

## Часть 6. Сервисы

Сервис — слой бизнес-логики. Находится между контроллером и репозиторием.

```java
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Notification createNotification(NotificationDto request) {
        // 1. Найти пользователя (бросит исключение если не найден)
        User user = userRepository.findById(request.getRecipientId()).orElseThrow();
        // 2. Создать объект уведомления
        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setStatus(NotificationStatus.CREATED);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRecipient(user);
        // 3. Сохранить в БД
        return notificationRepository.save(notification);
    }

    public Notification updateNotification(Long id, NotificationDto request) {
        Notification notification = notificationRepository.findById(id).orElseThrow();
        notification.setStatus(request.getStatus());
        // Автоматически ставим время отправки при статусе SENT
        if (request.getStatus() == NotificationStatus.SENT) {
            notification.setSentAt(LocalDateTime.now());
        }
        return notificationRepository.save(notification);
    }
}
```

### @Transactional
Гарантирует атомарность — либо все операции выполняются, либо ни одна:
```
Без @Transactional:  save() → ошибка → запись ОСТАЛАСЬ в БД
С @Transactional:    save() → ошибка → ROLLBACK → записи нет
```

---

## Часть 7. Контроллеры

Контроллер — HTTP-слой. Принимает запросы и возвращает ответы в JSON.

```java
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/add")
    public NotificationDto createNotification(@RequestBody @Valid NotificationDto request) {
        Notification n = notificationService.createNotification(request);
        return toDto(n);  // конвертируем Entity → DTO перед отдачей клиенту
    }

    @GetMapping("/{id}")
    public NotificationDto getById(@PathVariable Long id) {
        return toDto(notificationService.getNotificationById(id));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return "Уведомление удалено";
    }

    // Вспомогательный метод — избегаем повторения кода
    private NotificationDto toDto(Notification n) {
        return NotificationDto.builder()
                .title(n.getTitle())
                .channel(n.getChannel())
                .status(n.getStatus())
                .recipientId(n.getRecipient().getId())
                .build();
    }
}
```

Аннотации:
- `@RestController` — все методы возвращают JSON
- `@RequestMapping("/notifications")` — базовый путь
- `@PostMapping`, `@GetMapping`, `@PutMapping`, `@DeleteMapping` — HTTP-методы
- `@RequestBody` — тело запроса десериализуется из JSON
- `@PathVariable` — значение из URL (`/{id}`)
- `@Valid` — запускает валидацию DTO

---

## Часть 8. Валидация

Аннотации на полях DTO + `@Valid` в контроллере:

```java
// В DTO:
@NotBlank(message = "Имя не должно быть пустым")
@Size(max = 100)
private String name;

@Email(message = "Некорректный формат email")
@Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
private String email;

@Pattern(regexp = "^\\+?[0-9]{7,15}$")
private String phone;

// В контроллере:
public UserDto createUser(@RequestBody @Valid UserDto request) { ... }
```

Если данные не соответствуют — Spring автоматически возвращает `400 Bad Request`.

---

## Часть 9. Архитектура целиком

```
HTTP Request (JSON)
        ↓
@RestController (NotificationController)
  - @Valid валидирует DTO
  - вызывает Service
        ↓
@Service (NotificationService)
  - бизнес-логика
  - @Transactional — транзакции
  - вызывает Repository
        ↓
@Repository (NotificationRepository)
  - extends JpaRepository
  - Spring генерирует SQL
        ↓
Hibernate (ORM)
  - маппинг объект ↔ таблица
        ↓
PostgreSQL (таблицы users, notifications)
```

---

## Часть 10. Lombok

Lombok генерирует шаблонный код во время компиляции:

| Аннотация | Что генерирует | Зачем |
|---|---|---|
| `@Getter` | `getName()`, `getEmail()`, ... | Доступ к полям |
| `@Setter` | `setName()`, `setEmail()`, ... | Изменение полей |
| `@NoArgsConstructor` | `new User()` | Требуется JPA |
| `@AllArgsConstructor` | Конструктор со всеми полями | Удобное создание объектов |
| `@Builder` | `User.builder().name("Ivan").build()` | Паттерн строителя |
| `@RequiredArgsConstructor` | Конструктор для `final` полей | Инъекция зависимостей |

Без Lombok пришлось бы писать ~50 строк геттеров/сеттеров вручную на каждый класс.
