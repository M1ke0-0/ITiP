# Подробное описание технологий — Лабораторная работа 11

---

## 1. Maven

**Что это:** инструмент сборки Java-проектов.

**Зачем нужен:**
- Автоматически скачивает все библиотеки (Spring, Hibernate, Lombok и др.) из интернета
- Компилирует код, запускает тесты, собирает JAR-файл
- Управляет версиями зависимостей — не нужно вручную скачивать JAR-файлы

**Где используется в проекте:** файл `pom.xml` — описывает все зависимости и плагины.

**Ключевые понятия:**
- `<dependency>` — библиотека которую нужно подключить
- `<scope>runtime</scope>` — библиотека нужна только при запуске, не при компиляции (PostgreSQL драйвер)
- `<optional>true</optional>` — библиотека не попадает в финальный JAR (Lombok, DevTools)
- `<annotationProcessorPaths>` — путь к процессору аннотаций (нужен для Lombok)

---

## 2. Spring Boot

**Что это:** фреймворк для быстрого создания Java-приложений.

**Зачем нужен:**
- Убирает необходимость ручной настройки — всё конфигурируется автоматически
- Встроенный Tomcat — не нужно отдельно устанавливать сервер
- Автоматически создаёт и настраивает бины (объекты) на основе аннотаций

**Где используется в проекте:** `Application.java`
```java
@SpringBootApplication  // = @Configuration + @ComponentScan + @EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

**Что делает `@SpringBootApplication`:**
- `@Configuration` — этот класс содержит конфигурацию Spring
- `@ComponentScan` — сканирует все классы в пакете и создаёт бины из `@Service`, `@Repository`, `@Controller`
- `@EnableAutoConfiguration` — автоматически настраивает Hibernate, DataSource, Jackson и др.

**Инверсия управления (IoC):** Spring сам создаёт объекты и управляет их жизненным циклом. Разработчик только описывает зависимости через аннотации.

**Внедрение зависимостей (DI):** Spring автоматически передаёт нужные объекты в конструктор:
```java
@RequiredArgsConstructor  // генерирует конструктор
public class NotificationService {
    private final NotificationRepository repo;  // Spring сам передаст объект
}
```

---

## 3. Spring Web MVC (spring-boot-starter-webmvc)

**Что это:** модуль Spring для создания REST API.

**Зачем нужен:** обрабатывает HTTP-запросы и возвращает JSON-ответы.

**Где используется:** контроллеры `UserController`, `NotificationController`.

**Ключевые аннотации:**

| Аннотация | Назначение |
|---|---|
| `@RestController` | Класс — REST-контроллер. Все методы возвращают JSON |
| `@RequestMapping("/users")` | Базовый URL для всех методов класса |
| `@GetMapping` | Обрабатывает GET-запрос |
| `@PostMapping` | Обрабатывает POST-запрос |
| `@PutMapping` | Обрабатывает PUT-запрос |
| `@DeleteMapping` | Обрабатывает DELETE-запрос |
| `@RequestBody` | Десериализует JSON из тела запроса в Java-объект |
| `@PathVariable` | Извлекает значение из URL (`/users/{id}`) |
| `@RequestParam` | Извлекает параметр из URL (`/users?name=Ivan`) |

**Как работает цикл запроса:**
```
POST /users/add  { "name": "Ivan" }
        ↓
DispatcherServlet — главный обработчик всех запросов
        ↓
UserController.createUser(@RequestBody UserDto)
  — Jackson автоматически конвертирует JSON → UserDto
        ↓
Возвращает UserDto
  — Jackson автоматически конвертирует UserDto → JSON
        ↓
HTTP Response 200  { "name": "Ivan", ... }
```

**Jackson** — библиотека которая конвертирует Java-объекты в JSON и обратно. Подключается автоматически со Spring Web.

---

## 4. Spring Data JPA (spring-boot-starter-data-jpa)

**Что это:** модуль Spring для работы с реляционными базами данных через JPA.

**Зачем нужен:** убирает шаблонный код для работы с БД — не нужно писать SQL вручную для базовых операций.

**JPA (Java Persistence API)** — стандарт Java описывающий как объекты должны сохраняться в БД. Это только интерфейс/спецификация.

**Hibernate** — реализация JPA. Именно он:
- Генерирует SQL-запросы
- Создаёт/изменяет таблицы (`ddl-auto`)
- Маппит результаты запросов в Java-объекты

**Как работает репозиторий:**
```java
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Spring сам создаёт реализацию этого интерфейса
    // JpaRepository<Тип_сущности, Тип_первичного_ключа>
}
```

Spring Data JPA смотрит на интерфейс и во время запуска приложения создаёт реализацию со всеми методами.

**Query Derivation — как Spring читает имя метода:**
```
findBy  Status  And  Channel
  ↓       ↓     ↓      ↓
SELECT  WHERE status  AND  channel
```
Ключевые слова: `findBy`, `And`, `Or`, `OrderBy`, `Asc`, `Desc`, `Between`, `Like`, `Not`

**@Query — два вида:**

JPQL работает с Java-классами:
```java
// Notification — Java-класс, n.status — поле Java-объекта
@Query("select n from Notification n where n.status = :status")
```

Native SQL работает с таблицами БД:
```java
// notifications — имя таблицы, status — имя колонки
@Query(value = "select * from notifications where status = :status", nativeQuery = true)
```

**HikariCP** — пул соединений с БД. Вместо создания нового подключения при каждом запросе держит готовый набор соединений. Значительно ускоряет работу приложения.

---

## 5. PostgreSQL

**Что это:** реляционная система управления базами данных (РСУБД).

**Зачем нужна:** хранит данные в структурированном виде, поддерживает SQL, транзакции, внешние ключи.

**Как подключается:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/Lab_3_JAVA
spring.datasource.driver-class-name=org.postgresql.Driver
```

**JDBC-драйвер** (`postgresql` в pom.xml) — библиотека для низкоуровневого общения Java с PostgreSQL. Hibernate использует его внутри.

**Таблицы которые создал Hibernate:**
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(255),
    device_token VARCHAR(255),
    telegram_chat_id VARCHAR(255),
    created_at TIMESTAMP
);

CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    channel VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    sent_at TIMESTAMP,
    recipient_id BIGINT NOT NULL REFERENCES users(id)
);
```

**`ddl-auto` варианты:**
- `update` — обновляет структуру, данные сохраняются (используем в разработке)
- `create` — пересоздаёт таблицы при каждом запуске, данные теряются
- `validate` — только проверяет, ничего не меняет (продакшен)
- `none` — Hibernate не трогает БД вообще

---

## 6. Lombok

**Что это:** библиотека-процессор аннотаций. Генерирует код во время компиляции.

**Зачем нужен:** убирает шаблонный код — геттеры, сеттеры, конструкторы.

**Важно:** Lombok не работает в runtime — он генерирует Java-код до компиляции. Поэтому нужна настройка `annotationProcessorPaths` в `pom.xml`.

**Аннотации используемые в проекте:**

`@Getter` / `@Setter` — на классе `User`, `Notification`, DTO:
```java
// Без Lombok пришлось бы писать:
public String getName() { return name; }
public void setName(String name) { this.name = name; }
// ...и так для каждого поля
```

`@NoArgsConstructor` — обязателен для JPA:
```java
// JPA требует пустой конструктор для создания объектов при загрузке из БД
new User()  // Hibernate вызывает это, потом заполняет поля
```

`@AllArgsConstructor` — конструктор со всеми полями:
```java
new UserDto("Ivan", "ivan@mail.ru", "+79991234567", null, null, null)
```

`@Builder` — паттерн строителя (используется в контроллерах):
```java
UserDto.builder()
    .name("Ivan")
    .email("ivan@mail.ru")
    .build()
// Удобно когда не все поля нужно заполнять
```

`@RequiredArgsConstructor` — конструктор только для `final` полей:
```java
// Spring использует этот конструктор для внедрения зависимостей
public NotificationService(NotificationRepository repo, UserRepository userRepo) { ... }
```

---

## 7. Bean Validation (spring-boot-starter-validation)

**Что это:** стандарт Java для декларативной валидации данных.

**Зачем нужен:** проверяет входные данные до выполнения бизнес-логики. Клиент получает понятное сообщение об ошибке вместо исключения из глубины кода.

**Как работает:**
```
HTTP Request → @Valid в контроллере → проверяет аннотации на полях DTO
    ↓                                         ↓
данные корректны                       данные некорректны
    ↓                                         ↓
выполняет метод                    400 Bad Request + сообщение об ошибке
```

**Аннотации используемые в проекте:**

| Аннотация | Где | Что проверяет |
|---|---|---|
| `@NotBlank` | `name`, `email`, `title`, `message` | Строка не null и не пустая |
| `@Email` | `email` | Формат email (наличие @) |
| `@Pattern` | `email`, `phone` | Соответствие регулярному выражению |
| `@Size(max=100)` | `name` | Длина не более 100 символов |
| `@NotNull` | `channel`, `recipientId` | Значение не null |

**`@Valid` в контроллере** — без этой аннотации все валидационные аннотации на DTO игнорируются:
```java
public UserDto createUser(@RequestBody @Valid UserDto request) {
//                                   ^^^^^^ запускает валидацию
```

---

## 8. Spring DevTools (spring-boot-devtools)

**Что это:** инструмент для ускорения разработки.

**Зачем нужен:** автоматически перезапускает приложение при изменении файлов — не нужно вручную останавливать и запускать снова.

**scope runtime** — не попадает в финальный JAR приложения, используется только при разработке.

---

## 9. @Transactional

**Что это:** аннотация Spring для управления транзакциями БД.

**Транзакция** — набор операций с БД который выполняется как единое целое.

**ACID — свойства транзакций:**
- **A**tomicity (Атомарность) — либо все операции выполняются, либо ни одна
- **C**onsistency (Согласованность) — БД переходит из одного корректного состояния в другое
- **I**solation (Изолированность) — транзакции не мешают друг другу
- **D**urability (Надёжность) — зафиксированные данные сохраняются даже при сбое

**Пример из проекта:**
```java
@Transactional
public Notification createNotification(NotificationDto request) {
    User user = userRepository.findById(...).orElseThrow(); // запрос 1
    notificationRepository.save(notification);              // запрос 2
    // если здесь исключение → ROLLBACK обоих запросов
}
// если дошли сюда без исключений → COMMIT
```

**Без @Transactional:** каждый `save()` — отдельная транзакция. При ошибке после `save()` данные остаются в БД.

---

## 10. Архитектурный паттерн — Layered Architecture

Проект разделён на слои, каждый знает только о соседнем:

```
┌─────────────────────────────────────────┐
│  Controller  (@RestController)          │
│  — HTTP, JSON, валидация, маппинг DTO   │
├─────────────────────────────────────────┤
│  Service  (@Service)                    │
│  — бизнес-логика, транзакции            │
├─────────────────────────────────────────┤
│  Repository  (@Repository)              │
│  — SQL-запросы, работа с БД             │
├─────────────────────────────────────────┤
│  Database  (PostgreSQL)                 │
│  — хранение данных                      │
└─────────────────────────────────────────┘
```

**Преимущества:**
- Каждый слой можно изменить независимо (поменять БД — только Repository)
- Легко тестировать каждый слой отдельно
- Код читаемый и структурированный
