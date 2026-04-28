# Spring Security — Технологии и ответы на вопросы

## Использованные технологии

### Spring Security
Фреймворк безопасности для Java-приложений. Реализует:
- **Аутентификацию** — кто ты?
- **Авторизацию** — что тебе разрешено?

Работает как цепочка сервлет-фильтров, через которую проходит каждый HTTP-запрос до того, как он попадёт в контроллер.

### JWT (JSON Web Token)
Открытый стандарт (RFC 7519) для безопасной передачи информации между сторонами. Состоит из трёх частей, разделённых точкой:
```
Header.Payload.Signature
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdmFuQGV4YW1wbGUuY29tIn0.xxxxx
```
- **Header** — алгоритм подписи (HS256)
- **Payload** — данные (subject = email, exp = время истечения)
- **Signature** — HMAC-подпись для проверки целостности

Используется библиотека **jjwt 0.12.6**.

### BCrypt
Алгоритм хэширования паролей с встроенной «солью». Каждый раз генерирует разный хэш для одного пароля, что защищает от rainbow table атак. Стандарт де-факто для хранения паролей.

---

## Ответы на вопросы

### 1. Что такое аутентификация и чем она отличается от авторизации?

**Аутентификация** — процесс проверки *кто ты*. Пользователь предъявляет учётные данные (логин + пароль, токен), и система подтверждает его личность.

**Авторизация** — процесс проверки *что тебе можно делать*. Происходит после аутентификации: система проверяет, есть ли у пользователя права на выполнение конкретного действия.

> **Пример:** Ты показываешь паспорт на входе (аутентификация). Охранник проверяет, есть ли у тебя пропуск в VIP-зону (авторизация).

В Spring Security:
- Аутентификация → `AuthenticationManager`, `UsernamePasswordAuthenticationToken`
- Авторизация → `@PreAuthorize`, `hasRole()`, `requestMatchers().hasRole()`

---

### 2. Что делает `SecurityFilterChain`?

`SecurityFilterChain` — это бин, который описывает **правила безопасности** для HTTP-запросов. Определяет:
- Какие URL требуют аутентификации, а какие открыты
- Какие роли нужны для доступа к конкретным путям
- Политику сессий (stateful/stateless)
- Какие фильтры включены (JWT-фильтр, форма логина, Basic Auth)

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()       // открыто
            .requestMatchers("/admin/**").hasRole("ADMIN") // только ADMIN
            .anyRequest().authenticated()                   // остальное — требует логина
        )
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
}
```

---

### 3. Зачем нужен `UserDetailsService`?

`UserDetailsService` — интерфейс с одним методом `loadUserByUsername(String username)`. Это **DAO-слой для Spring Security**: он знает, где хранятся пользователи (БД, LDAP, память) и как их загрузить.

Spring Security **не проверяет пароль** в этом методе — только загружает пользователя. Сравнение пароля делает `DaoAuthenticationProvider` через `PasswordEncoder`.

Наша реализация — `CustomUserDetailsService`:
```java
public UserDetails loadUserByUsername(String username) {
    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("Не найден"));
    return new CustomUserDetails(user);
}
```

---

### 4. Для чего используется `PasswordEncoder`?

`PasswordEncoder` — интерфейс для хэширования и сравнения паролей. Мы используем `BCryptPasswordEncoder`.

| Метод | Описание |
|-------|----------|
| `encode(rawPassword)` | хэширует пароль при регистрации |
| `matches(rawPassword, encoded)` | сравнивает введённый пароль с хэшем из БД |

```java
// При регистрации
user.setPassword(passwordEncoder.encode("qwerty123"));
// Результат: $2a$10$XXXXX...

// При логине (делает DaoAuthenticationProvider автоматически)
passwordEncoder.matches("qwerty123", "$2a$10$XXXXX..."); // → true
```

---

### 5. Почему пароли нельзя хранить в открытом виде?

Несколько причин:

1. **Утечка данных**: если злоумышленник получит доступ к БД — он сразу получит все пароли
2. **Повторное использование**: большинство людей используют одинаковые пароли на разных сайтах
3. **Внутренние угрозы**: администраторы БД не должны видеть пароли пользователей
4. **Законодательство**: GDPR и другие нормативы требуют защиты персональных данных

BCrypt решает проблему: даже если хэш утёк — восстановить пароль из него практически невозможно за разумное время. Встроенная «соль» делает каждый хэш уникальным, что исключает атаки через предвычисленные таблицы.

---

### 6. Какую роль выполняет `AuthenticationManager`?

`AuthenticationManager` — центральный интерфейс для запуска аутентификации. Принимает объект `Authentication` (с логином и паролем), делегирует проверку провайдерам, и возвращает заполненный `Authentication` (с ролями) если успех, или бросает исключение при ошибке.

В нашем коде используется в `AuthController`:
```java
authenticationManager.authenticate(
    new UsernamePasswordAuthenticationToken(email, password)
);
```

Это запускает цепочку: `AuthenticationManager` → `DaoAuthenticationProvider` → `CustomUserDetailsService` → `PasswordEncoder`.

---

### 7. Что делает `DaoAuthenticationProvider`?

`DaoAuthenticationProvider` — стандартная реализация `AuthenticationProvider`, которая:
1. Загружает пользователя через `UserDetailsService.loadUserByUsername()`
2. Сравнивает введённый пароль с хэшем через `PasswordEncoder.matches()`
3. Возвращает `Authentication` с ролями при успехе
4. Бросает `BadCredentialsException` при неверном пароле

В Spring Security 7 создаётся с `UserDetailsService` через конструктор:
```java
DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
provider.setPasswordEncoder(passwordEncoder());
```

---

### 8. Как работает HTTP Basic?

HTTP Basic — простейший механизм аутентификации. Клиент кодирует `login:password` в Base64 и отправляет в заголовке каждого запроса:

```
Authorization: Basic aXZhbkBleGFtcGxlLmNvbTpxd2VydHkxMjM=
```

Spring Security автоматически декодирует этот заголовок через `BasicAuthenticationFilter`.

> ⚠️ **Важно**: Base64 — это НЕ шифрование. Без HTTPS пароль можно перехватить. В нашем проекте мы **отключили** HTTP Basic (`httpBasic.disable()`) и полностью перешли на JWT.

---

### 9. Что хранится в `SecurityContext`?

`SecurityContext` хранит объект `Authentication` для **текущего запроса/потока**. `Authentication` содержит:
- `principal` — объект `UserDetails` (наш `CustomUserDetails`)
- `credentials` — обычно `null` после аутентификации (пароль очищается)
- `authorities` — список ролей (`ROLE_USER`, `ROLE_ADMIN`)
- `isAuthenticated()` — флаг успешной аутентификации

Доступ к текущему пользователю:
```java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
User user = userDetails.getUser();
```

В STATELESS режиме `SecurityContext` живёт только в рамках одного HTTP-запроса и не сохраняется между запросами.

---

### 10. Как работает аутентификация через сессию?

1. Пользователь отправляет логин + пароль
2. Spring Security проверяет их и создаёт `Authentication`
3. `Authentication` сохраняется в `SecurityContext`
4. `SecurityContext` помещается в HTTP-сессию на сервере
5. Клиент получает cookie `JSESSIONID`
6. В следующих запросах браузер автоматически отправляет `JSESSIONID`
7. Сервер восстанавливает `SecurityContext` из сессии по этому ID

**Плюсы**: удобно для браузерных приложений  
**Минусы**: сервер хранит состояние → сложнее масштабировать, плохо подходит для REST API и мобильных клиентов

---

### 11. Что такое JWT и чем он отличается от сессии?

| Параметр | Сессия | JWT |
|----------|--------|-----|
| Где хранится состояние | На сервере (в памяти/БД) | В токене у клиента |
| Что отправляет клиент | Cookie `JSESSIONID` | Заголовок `Authorization: Bearer <token>` |
| Масштабируемость | Сложно (нужен shared session store) | Легко (сервер stateless) |
| Инвалидация | Мгновенная (удалить сессию) | Только через blacklist или expiration |
| Подходит для | Браузерные web-приложения | REST API, микросервисы, мобильные |

**JWT** — самодостаточный токен: он содержит всю нужную информацию (кто пользователь, когда истекает) и подписан секретным ключом. Сервер не хранит ничего — только проверяет подпись.

---

### 12. Зачем нужен JWT-фильтр?

`JwtAuthenticationFilter` — это звено в цепочке Spring Security, которое **переводит JWT-токен в объект Authentication**.

Без этого фильтра Spring Security не знает, как читать наши токены. Стандартный `BasicAuthenticationFilter` понимает только Basic Auth.

Алгоритм работы фильтра:
```
Запрос → Есть заголовок "Authorization: Bearer ..."?
           ↓ Нет → пропустить дальше
           ↓ Да
         Извлечь токен (substring(7))
         Вызвать jwtService.extractUsername(token)
         Токен валиден? → Загрузить UserDetails
         Создать UsernamePasswordAuthenticationToken
         Поместить в SecurityContext
         → Следующий фильтр → Контроллер
```

---

### 13. Что делает `addFilterBefore()` в конфигурации?

```java
http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
```

Вставляет наш `JwtAuthenticationFilter` **перед** стандартным `UsernamePasswordAuthenticationFilter` в цепочке фильтров. Это важно потому, что:

- `UsernamePasswordAuthenticationFilter` обрабатывает логин через форму
- Наш JWT-фильтр должен сработать **раньше** и заполнить `SecurityContext`
- Если `SecurityContext` уже заполнен — остальные фильтры аутентификации пропускают запрос

---

### 14. В чём разница между `SessionCreationPolicy.IF_REQUIRED` и `SessionCreationPolicy.STATELESS`?

| Политика | Поведение |
|----------|-----------|
| `IF_REQUIRED` | Сессия создаётся **только если нужна** (например, при логине через форму). По умолчанию в Spring Security. |
| `STATELESS` | Сессия **никогда не создаётся и не используется**. Каждый запрос аутентифицируется заново (через JWT). |

В REST API с JWT всегда используют `STATELESS`:
```java
.sessionManagement(session -> session
    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
)
```

Это гарантирует, что сервер не хранит состояние и можно легко масштабировать горизонтально (несколько инстансов не нужно синхронизировать).

---

### 15. Как проверить доступ к URL по роли пользователя?

**Способ 1 — через `SecurityFilterChain` (на уровне URL):**
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/admin/**").hasRole("ADMIN")           // только ADMIN
    .requestMatchers("/users/**").hasAnyRole("USER", "ADMIN") // USER или ADMIN
    .anyRequest().authenticated()
)
```
> ⚠️ `hasRole("ADMIN")` автоматически добавляет префикс `ROLE_`, поэтому в БД должно быть `ROLE_ADMIN`.

**Способ 2 — через `@PreAuthorize` (на уровне метода):**
```java
@PreAuthorize("hasRole('ADMIN')")
public void deleteUser(Long id) { ... }
```

Требует `@EnableMethodSecurity` в конфигурации (уже добавлено).

**Способ 3 — через `@Secured`:**
```java
@Secured("ROLE_ADMIN")
public void someAdminMethod() { ... }
```

Оба метода (1 и 2) можно комбинировать — это даёт двойную защиту.
