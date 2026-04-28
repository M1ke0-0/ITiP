# Spring Security — Обзор реализации

## Что мы делали

В этой лабораторной работе мы поэтапно добавили **Spring Security** с **JWT-аутентификацией** к существующему REST API уведомлений. Приложение теперь требует аутентификации для большинства эндпоинтов, поддерживает роли пользователей (`ROLE_USER`, `ROLE_ADMIN`) и выдаёт JWT-токены вместо хранения сессий на сервере.

---

## Структура изменений

### Часть 1 — Зависимости (`pom.xml`)

Добавлены четыре зависимости:

```xml
<!-- Spring Security — основной стартер -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT: API-интерфейсы -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>

<!-- JWT: реализация (runtime) -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>

<!-- JWT: сериализация через Jackson (runtime) -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
```

---

### Часть 2 — Обновление сущности и DTO

#### `model/entity/User.java` — обновлён
Добавлены три новых поля для работы с безопасностью:

| Поле | Тип | Описание |
|------|-----|----------|
| `password` | `String` | Хэшированный пароль (BCrypt) |
| `role` | `UserRole` | Роль пользователя в системе |
| `email` | `String` (unique) | Используется как логин |

#### `model/enums/UserRole.java` — создан
Перечисление ролей:
```java
public enum UserRole {
    ROLE_USER,   // обычный пользователь
    ROLE_ADMIN   // администратор
}
```
Префикс `ROLE_` обязателен — Spring Security использует его при проверке через `hasRole("ADMIN")`.

#### `model/dto/RegisterRequest.java` — создан
DTO для регистрации: `name`, `email` (@Email), `password` (@Size min=6).

#### `model/dto/LoginRequest.java` — создан
DTO для логина: `email` (@Email), `password`.

---

### Часть 3 — Репозиторий и загрузка пользователя

#### `repository/UserRepository.java` — обновлён
Добавлен метод поиска по email (используется Spring Data JPA):
```java
Optional<User> findByEmail(String email);
```

#### `security/CustomUserDetails.java` — создан
**Адаптер** между сущностью `User` и интерфейсом `UserDetails` Spring Security.

| Метод | Что возвращает |
|-------|---------------|
| `getUsername()` | email пользователя |
| `getPassword()` | хэш пароля из БД |
| `getAuthorities()` | роль как `SimpleGrantedAuthority` |
| `isEnabled()` / `isAccountNonLocked()` / ... | всегда `true` |
| `getUser()` | исходная сущность `User` |

#### `security/CustomUserDetailsService.java` — создан
Сервис загрузки пользователя по логину. Реализует `UserDetailsService`:
```java
public UserDetails loadUserByUsername(String username) {
    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    return new CustomUserDetails(user);
}
```
Spring Security вызывает этот метод при каждой попытке аутентификации.

---

### Часть 4 — Конфигурация безопасности

#### `config/SecurityConfig.java` — создан
Центральный класс конфигурации. Регистрирует все бины безопасности:

| Бин | Назначение |
|-----|------------|
| `PasswordEncoder` | BCrypt для хэширования паролей |
| `DaoAuthenticationProvider` | связывает `UserDetailsService` + `PasswordEncoder` |
| `AuthenticationManager` | управляет аутентификацией в `AuthController` |
| `SecurityFilterChain` | правила доступа к URL + цепочка фильтров |

Правила доступа:
```
/auth/**             → всем (регистрация, логин)
/admin/**            → только ROLE_ADMIN
/users/**            → ROLE_USER и ROLE_ADMIN
/notifications/**    → ROLE_USER и ROLE_ADMIN
остальное            → требует аутентификации
```

Режим сессий: `SessionCreationPolicy.STATELESS` — сервер не хранит состояние.

---

### Часть 5 — Регистрация и логин

#### `service/AuthService.java` — создан
Бизнес-логика регистрации:
- `register(RegisterRequest)` — создаёт пользователя с ролью `ROLE_USER`
- `registerAdmin(RegisterRequest)` — создаёт администратора (только для существующих ADMIN)
- Перед сохранением проверяет уникальность email → бросает `EmailAlreadyExistsException`
- Пароль хэшируется через `passwordEncoder.encode()`

#### `controller/AuthController.java` — создан

| Метод | URL | Доступ | Описание |
|-------|-----|--------|----------|
| `POST` | `/auth/register` | все | Регистрация пользователя |
| `POST` | `/auth/register/admin` | ADMIN | Регистрация администратора |
| `POST` | `/auth/login` | все | Логин, возвращает JWT токен |

---

### Часть 6 — JWT

#### `security/JwtService.java` — создан
Генерация и валидация JWT-токенов (jjwt 0.12.6):

| Метод | Описание |
|-------|----------|
| `generateToken(username)` | создаёт токен, действует 1 час |
| `extractUsername(token)` | достаёт subject (email) из токена |
| `isTokenValid(token)` | проверяет, не истёк ли срок |
| `parseClaims(token)` | парсит и верифицирует подпись |

#### `security/JwtAuthenticationFilter.java` — создан
Фильтр, срабатывающий на каждый HTTP-запрос (`OncePerRequestFilter`):
1. Читает заголовок `Authorization: Bearer <token>`
2. Извлекает username из токена
3. Загружает пользователя через `UserDetailsService`
4. Помещает `Authentication` в `SecurityContext`

---

### Часть 7 — Дополнительные улучшения

#### `model/mapper/UserMapper.java` — создан
Преобразует `User` → `UserDto`. Убирает дублирование builder-кода из всех методов контроллера:
```java
public UserDto toDto(User user) {
    return UserDto.builder()
        .name(user.getName())
        // ...
        .build();
}
```

#### `exception/EmailAlreadyExistsException.java` — создан
Кастомное исключение при дублировании email. Перехватывается в `GlobalExceptionHandler` → HTTP 409 Conflict.

#### `exception/GlobalExceptionHandler.java` — создан
Централизованная обработка ошибок (`@RestControllerAdvice`):

| Исключение | HTTP статус | Сообщение |
|-----------|-------------|-----------|
| `BadCredentialsException` | 401 Unauthorized | Неверный email или пароль |
| `EmailAlreadyExistsException` | 409 Conflict | Пользователь уже существует |
| `MethodArgumentNotValidException` | 400 Bad Request | Поля с ошибками валидации |
| `Exception` | 500 Internal Server Error | Общие ошибки |

#### `controller/AdminController.java` — создан
Тестовый контроллер `/admin/ping` для проверки авторизации по роли.

#### `service/UserService.java` — обновлён
Метод `deleteUser()` защищён аннотацией:
```java
@PreAuthorize("hasRole('ADMIN')")
public void deleteUser(Long id) { ... }
```

#### `controller/UserController.java` — обновлён
Использует `UserMapper` вместо дублирующегося кода в каждом методе.

---

## Итоговая карта файлов

```
src/main/java/spring_lab3_notifications/org/example/
│
├── config/
│   ├── AppConfig.java              (был)
│   └── SecurityConfig.java         [НОВЫЙ] — конфигурация безопасности
│
├── controller/
│   ├── AdminController.java        [НОВЫЙ] — /admin/**
│   ├── AuthController.java         [НОВЫЙ] — /auth/register, /auth/login
│   ├── HelloController.java        (был)
│   ├── NotificationController.java (был)
│   └── UserController.java         [ОБНОВЛЁН] — использует UserMapper
│
├── exception/
│   ├── EmailAlreadyExistsException.java  [НОВЫЙ]
│   └── GlobalExceptionHandler.java       [НОВЫЙ]
│
├── model/
│   ├── dto/
│   │   ├── LoginRequest.java       [НОВЫЙ]
│   │   ├── NotificationDto.java    (был)
│   │   ├── RegisterRequest.java    [НОВЫЙ]
│   │   └── UserDto.java            (был)
│   ├── entity/
│   │   ├── Notification.java       (был)
│   │   └── User.java               [ОБНОВЛЁН] — +password, +role
│   ├── enums/
│   │   └── UserRole.java           [НОВЫЙ]
│   └── mapper/
│       └── UserMapper.java         [НОВЫЙ]
│
├── repository/
│   ├── NotificationRepository.java (был)
│   └── UserRepository.java         [ОБНОВЛЁН] — +findByEmail()
│
├── security/
│   ├── CustomUserDetails.java          [НОВЫЙ] — адаптер User→UserDetails
│   ├── CustomUserDetailsService.java   [НОВЫЙ] — загрузка по email
│   ├── JwtAuthenticationFilter.java    [НОВЫЙ] — фильтр Bearer токена
│   └── JwtService.java                 [НОВЫЙ] — генерация/валидация JWT
│
└── service/
    ├── AuthService.java            [НОВЫЙ] — регистрация
    ├── NotificationService.java    (был)
    ├── UserService.java            [ОБНОВЛЁН] — @PreAuthorize на deleteUser
    └── ...
```
