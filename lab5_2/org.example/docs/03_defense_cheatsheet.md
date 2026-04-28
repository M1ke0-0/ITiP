# Шпаргалка для защиты — Spring Security + JWT

## Быстрые ответы (для защиты)

### Зачем нужен Spring Security?
> Это фреймворк, который защищает приложение: требует аутентификации, проверяет роли и обрабатывает каждый запрос через цепочку фильтров до того, как он попадёт в контроллер.

### Почему email как логин?
> Email уникален (`unique = true` в схеме БД), и пользователи помнят его лучше, чем username. Spring Security требует логин-строку — мы передаём email через `getUsername()` в `CustomUserDetails`.

### Зачем `CustomUserDetails`, если есть встроенный `User` Spring Security?
> Встроенный `User` ограничен: у него нет доступа к нашей сущности. `CustomUserDetails` — адаптер, который позволяет хранить ссылку на `User` и получать доступ к телефону, токену устройства и другим полям при необходимости.

### Как Spring Security узнаёт о пользователях в нашей БД?
> Через `CustomUserDetailsService.loadUserByUsername()`. Этот метод вызывается при каждой аутентификации. Мы ищем по email в `UserRepository`.

### Почему пароль хранится как хэш?
> BCrypt необратим. Даже при утечке БД злоумышленник не сможет восстановить пароли. `PasswordEncoder.matches()` сравнивает введённый пароль с хэшем без расшифровки.

### Как работает цепочка при логине?
```
POST /auth/login
→ AuthController.login()
→ authenticationManager.authenticate(email, password)
→ DaoAuthenticationProvider
  → CustomUserDetailsService.loadUserByUsername(email)    // загрузить пользователя
  → passwordEncoder.matches(password, user.getPassword()) // сравнить пароль
→ Успех → jwtService.generateToken(email)
→ Вернуть JWT клиенту
```

### Как работает цепочка при обращении к защищённому URL?
```
GET /users/all  [Authorization: Bearer eyJ...]
→ JwtAuthenticationFilter.doFilterInternal()
  → Извлечь токен из заголовка
  → jwtService.extractUsername(token) → email
  → jwtService.isTokenValid(token)    → true
  → userDetailsService.loadUserByUsername(email)
  → SecurityContextHolder.setAuthentication(...)
→ SecurityFilterChain проверяет права
→ UserController.getAllUsers()
```

---

## Карта эндпоинтов

| Метод | URL | Роль | Описание |
|-------|-----|------|----------|
| `POST` | `/auth/register` | Все | Регистрация пользователя |
| `POST` | `/auth/register/admin` | ADMIN | Регистрация администратора |
| `POST` | `/auth/login` | Все | Логин → JWT токен |
| `GET` | `/users/all` | USER, ADMIN | Список всех пользователей |
| `GET` | `/users/{id}` | USER, ADMIN | Пользователь по ID |
| `POST` | `/users/add` | USER, ADMIN | Создать пользователя |
| `PUT` | `/users/{id}` | USER, ADMIN | Обновить пользователя |
| `DELETE` | `/users/{id}` | **ADMIN** | Удалить пользователя |
| `GET` | `/admin/ping` | **ADMIN** | Тест доступа администратора |
| `GET` | `/notifications/**` | USER, ADMIN | Уведомления |

---

## Тест в Postman — пошагово

### Шаг 1: Зарегистрироваться
```http
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "name": "Иван Иванов",
  "email": "ivan@example.com",
  "password": "qwerty123"
}
```
Ожидаемый ответ: `"Пользователь успешно зарегистрирован"`

### Шаг 2: Войти и получить токен
```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "ivan@example.com",
  "password": "qwerty123"
}
```
Ожидаемый ответ: `eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpd...`

### Шаг 3: Использовать токен
```http
GET http://localhost:8080/users/all
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpd...
```

### Шаг 4: Проверить ошибку авторизации
```http
GET http://localhost:8080/admin/ping
Authorization: Bearer <токен_пользователя_с_ROLE_USER>
```
Ожидаемый ответ: `403 Forbidden`

### Шаг 5: Проверить неверный пароль
```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "ivan@example.com",
  "password": "неверный_пароль"
}
```
Ожидаемый ответ:
```json
{ "error": "Неверный email или пароль" }
```

### Шаг 6: Проверить дубль email
```http
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "name": "Другой Иван",
  "email": "ivan@example.com",
  "password": "password123"
}
```
Ожидаемый ответ: `409 Conflict`
```json
{ "error": "Пользователь с email ivan@example.com уже существует" }
```

---

## Типичные вопросы на защите

**В: Что будет, если убрать `@EnableMethodSecurity`?**
> Аннотации `@PreAuthorize` и `@Secured` перестанут работать. Защита на уровне методов сервиса отключится, хотя URL-правила в `SecurityFilterChain` останутся.

**В: Что произойдёт если не добавить `JwtAuthenticationFilter` в цепочку?**
> Сервер не будет понимать JWT. Каждый запрос с токеном будет возвращать 401/403, потому что `SecurityContext` останется пустым.

**В: Почему `jjwt-impl` и `jjwt-jackson` указаны как `runtime`?**
> Они нужны только во время выполнения (для подписи и сериализации). Компилировать код против них не нужно — все публичные интерфейсы находятся в `jjwt-api`.

**В: Как токен защищён от подделки?**
> Токен подписан HMAC-SHA256 с секретным ключом. При парсинге `Jwts.parser().verifyWith(key).build()` проверяет подпись — если токен изменён, бросается `JwtException`.

**В: Как инвалидировать JWT токен?**
> В базовой реализации — никак (сервер stateless). Стандартные подходы: хранить blacklist отозванных токенов в Redis, или использовать короткий срок жизни (1 час у нас) + refresh tokens.

**В: Чем `@PreAuthorize("hasRole('ADMIN')")` отличается от `requestMatchers("/admin/**").hasRole("ADMIN")`?**
> `requestMatchers` — проверка на уровне URL в фильтре (грубее, быстрее). `@PreAuthorize` — на уровне метода (тоньше, можно использовать SpEL-выражения с параметрами). Комбинирование даёт двойную защиту.

**В: Почему в `getAuthorities()` мы создаём `SimpleGrantedAuthority(user.getRole().name())`?**
> `SimpleGrantedAuthority` принимает строку-роль. Метод `name()` у enum возвращает `"ROLE_ADMIN"` или `"ROLE_USER"`. Именно этот формат с префиксом `ROLE_` ожидает Spring Security.

**В: Что такое `SecurityContextHolder` и зачем он нужен?**
> Хранит `SecurityContext` для **текущего потока** (ThreadLocal). Это позволяет из любого места приложения (сервис, контроллер) получить информацию о текущем пользователе без передачи её через параметры методов.

**В: Что произойдёт, если токен истёк?**
> `jwtService.isTokenValid()` вернёт `false`. Фильтр не установит `Authentication` в `SecurityContext`. Spring Security вернёт `401 Unauthorized`.

---

## Ключевые классы и их связи

```
HTTP-запрос
    ↓
JwtAuthenticationFilter          ← читает Bearer токен
    ↓ extractUsername()
JwtService                       ← парсит/генерирует JWT
    ↓ loadUserByUsername()
CustomUserDetailsService         ← загружает из БД
    ↓
UserRepository.findByEmail()     ← Spring Data JPA
    ↓
CustomUserDetails                ← адаптер User → UserDetails
    ↓
SecurityContextHolder             ← хранит Authentication
    ↓
SecurityFilterChain              ← проверяет права по URL
    ↓
@PreAuthorize                    ← проверяет права на метод
    ↓
Controller → Service → Repository
```

---

## Структура JWT-токена

```
eyJhbGciOiJIUzI1NiJ9          ← Header (Base64)
.
eyJzdWIiOiJpdmFuQGV4YW1wbGUuY29tIiwiaWF0IjoxNjAwMDAwMDAwLCJleHAiOjE2MDAwMDM2MDB9
                                ← Payload (Base64): sub, iat, exp
.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
                                ← Signature (HMAC-SHA256)
```

Декодированный payload:
```json
{
  "sub": "ivan@example.com",
  "iat": 1700000000,
  "exp": 1700003600
}
```

---

## Коды ответов API

| Код | Ситуация |
|-----|----------|
| `200 OK` | Успешный запрос |
| `400 Bad Request` | Ошибка валидации (`@Valid`) |
| `401 Unauthorized` | Неверный пароль или нет токена |
| `403 Forbidden` | Нет прав (неверная роль) |
| `409 Conflict` | Email уже зарегистрирован |
| `500 Internal Server Error` | Неожиданная ошибка сервера |
