# Шпаргалка: Spring Boot IoC/DI (Лаб. №3)

---

## Ключевые понятия

| Термин | Смысл |
|--------|-------|
| **Bean** | Объект, созданный и управляемый Spring IoC-контейнером |
| **IoC** (Inversion of Control) | Управление созданием объектов передаётся контейнеру, а не коду |
| **DI** (Dependency Injection) | Зависимости передаются объекту снаружи, а не создаются внутри |
| **ApplicationContext** | Spring-контейнер — хранит все бины и управляет ими |

---

## Аннотации — быстрая таблица

| Аннотация | Где ставится | Что делает |
|-----------|-------------|------------|
| `@SpringBootApplication` | Главный класс | Запускает Spring Boot (включает @ComponentScan, @EnableAutoConfiguration, @Configuration) |
| `@RestController` | Класс-контроллер | Обрабатывает HTTP, автоматически возвращает JSON/String |
| `@GetMapping("/path")` | Метод | Маппит GET-запрос на метод |
| `@RequestParam` | Параметр метода | Получает query-параметр из URL (?name=value) |
| `@Service` | Класс-сервис | Делает класс бином, семантика: бизнес-логика |
| `@Repository` | Класс DAO | Бин + трансляция исключений БД |
| `@Controller` | MVC-контроллер | Бин, обрабатывает запросы |
| `@Component` | Любой класс | Универсальный бин |
| `@Configuration` | Класс конфигурации | Источник бинов для контейнера |
| `@Bean` | Метод в @Configuration | Возвращаемый объект становится бином |
| `@Import(Config.class)` | Класс | Подключает другой конфиг-класс |
| `@ComponentScan("pkg")` | @Configuration | Указывает пакет для поиска @Service/@Component и т.д. |
| `@Autowired` | Конструктор/поле/сеттер | Внедряет зависимость (на конструкторе можно опустить) |
| `@Primary` | Класс-бин | Этот бин выбирается по умолчанию при неоднозначности |
| `@Qualifier("name")` | Параметр конструктора | Явно указывает имя бина для внедрения |

---

## Три способа конфигурации Spring

### 1. Java Config — явное объявление бинов
```java
@Configuration
public class AppConfig {
    @Bean
    public EmailService emailService() {
        return new EmailService();
    }

    @Bean
    public NotificationManager notificationManager() {
        return new NotificationManager(emailService()); // явный выбор
    }
}
```
**Плюс:** полный контроль. **Минус:** много кода.

---

### 2. Аннотации + ComponentScan — автоматическое обнаружение
```java
@Service
public class EmailService implements MessageService { ... }

@Configuration
@ComponentScan("org.example")
public class AppConfig { }
```
**Плюс:** минимум кода. **Минус:** может вызвать конфликт при нескольких бинах одного типа.

---

### 3. Смешанный подход
`@ComponentScan` для своих классов + `@Bean` для внешних библиотек.

---

## Три вида DI (внедрения зависимостей)

```java
// 1. Через конструктор (ЛУЧШИЙ способ)
@Service
public class NotificationManager {
    private final MessageService service;

    public NotificationManager(MessageService service) {
        this.service = service;
    }
}

// 2. Через сеттер
@Autowired
public void setService(MessageService service) { ... }

// 3. Через поле (не рекомендуется — нельзя final, плохо тестируется)
@Autowired
private MessageService service;
```

---

## Разрешение конфликта нескольких бинов одного типа

**Проблема:** два класса реализуют один интерфейс → Spring не знает, какой внедрить.
```
NoSuchBeanDefinitionException: expected single matching bean but found 2
```

### Способ 1: @Primary — приоритетный бин
```java
@Service
@Primary                   // выбирается по умолчанию
public class SmsService implements MessageService { ... }
```

### Способ 2: @Qualifier — явное указание имени
```java
@Autowired
public NotificationManager(@Qualifier("smsService") MessageService service) { ... }
```
Имя бина можно задать: `@Service("customName")` → `@Qualifier("customName")`.

### Способ 3: Коллекция — все бины сразу (самый гибкий)
```java
@Autowired
public NotificationManager(List<MessageService> services) {
    // Spring внедрит ВСЕ бины типа MessageService
}

// Или Map: ключ = имя бина, значение = сам бин
@Autowired
public NotificationManager(Map<String, MessageService> serviceMap) {
    // serviceMap.get("smsService") → SmsService
}
```

---

## Иерархия аннотаций-компонентов

```
@Component
├── @Service       (бизнес-логика)
├── @Repository    (данные, + трансляция исключений)
└── @Controller    (HTTP, + @RestController = @Controller + @ResponseBody)
```
Все они технически одинаковы с точки зрения IoC; разница — семантическая и поведенческая (для `@Repository`).

---

## Типичные ошибки и их причины

| Ошибка | Причина |
|--------|---------|
| `No qualifying bean of type '...' available` | Нет бина нужного типа (не @Service, неверный пакет в @ComponentScan) |
| `expected single matching bean but found 2` | Два кандидата, нет @Primary/@Qualifier |
| `NoSuchBeanDefinitionException` | @Qualifier указывает несуществующее имя |
| `UnsatisfiedDependencyException` | Spring не смог удовлетворить зависимость в конструкторе |

---

## Преимущества IoC/DI

1. **Слабая связанность** — компоненты зависят от интерфейсов, а не реализаций
2. **Тестируемость** — легко подменить зависимость моком
3. **Расширяемость** — добавить новый сервис = создать класс с `@Service`, без правки существующего кода
4. **Читаемость** — зависимости явно видны в конструкторе

---

## Структура проекта лабораторной

```
org.example
├── Application.java              (@SpringBootApplication + @Import)
├── config/
│   └── AppConfig.java            (@Configuration + @ComponentScan)
├── controller/
│   ├── HelloController.java      (/hello, /goodbye, /greet, /user-info)
│   └── NotificationController.java  (/notify, /notify-via)
└── service/
    ├── MessageService.java       (интерфейс)
    ├── AdvancedMessageService.java (расширенный интерфейс, доп.задание)
    ├── EmailService.java         (@Service("customEmail"))
    ├── SmsService.java           (@Service + @Primary)
    ├── PushService.java          (@Service)
    ├── TelegramService.java      (@Service, реализует AdvancedMessageService)
    └── NotificationManager.java  (@Service, List<> + Map<>)
```

---

## Эндпоинты для демонстрации

```
GET /hello
GET /goodbye
GET /greet?name=Иван
GET /user-info?name=Иван&age=20

GET /notify?message=Hello&email=test@test.com
    → отправляет через ВСЕ сервисы (List<MessageService>)

GET /notify-via?service=smsService&message=Hello&recipient=test@test.com
    → отправляет только через smsService (Map<String, MessageService>)
    Доступные имена: customEmail, smsService, pushService, telegramService
```

---

## 🎯 ПОДГОТОВКА К ЗАЩИТЕ: "Коварные" вопросы и Live Review

### ❓ Топ "коварных" вопросов от преподавателя

1.  **"Что будет, если я уберу @Service над EmailService, но оставлю его в Map в NotificationManager?"**
    *   *Ответ:* При запуске возникнет ошибка `NoSuchBeanDefinitionException` или сервис просто не попадет в Map, так как Spring не создаст бин для этого класса и не найдет его при сканировании.
2.  **"Почему в NotificationManager используется и List, и Map? Разве одного List мало?"**
    *   *Ответ:* `List` удобен для рассылки всем (метод `notify`), а `Map` позволяет быстро найти конкретный сервис по имени (метод `notifyVia`) без ручного перебора списка.
3.  **"Твой NotificationManager и NotificationController зависят от интерфейса MessageService. Почему это хорошо?"**
    *   *Ответ:* Это принцип **DIP (Dependency Inversion)**. Мы можем добавить 10 новых типов уведомлений, не меняя ни строчки кода в контроллере или менеджере.
4.  **"Где именно в твоем проекте происходит Inversion of Control?"**
    *   *Ответ:* В классе `AppConfig` (через `@ComponentScan`) или прямо в `Application.java`. Мы не пишем `new EmailService()`, это делает Spring за нас.
5.  **"Что произойдет, если в системе будет два бина с именем 'smsService'?"**
    *   *Ответ:* Spring выдаст ошибку `ConflictingBeanDefinitionException` при старте, так как имена бинов в контексте должны быть уникальны.

### 🛠 Сценарии Live Review (Что могут заставить сделать)

**Задание 1: "Сделай так, чтобы по умолчанию всегда использовался TelegramService"**
*   **Что делать:** Добавь аннотацию `@Primary` над классом `TelegramService`.
*   **Зачем:** Чтобы показать, что ты понимаешь, как разрешать неоднозначность (Ambiguity).

**Задание 2: "Добавь новый метод уведомления (например, Discord), не меняя NotificationManager"**
*   **Что делать:** Просто создай `DiscordService implements MessageService { ... }` с аннотацией `@Service`. 
*   **Зачем:** Показать мощь `@Autowired` коллекции (List/Map) — новый бин подхватится автоматически.

**Задание 3: "Смени внедрение через конструктор на @Autowired на поле в контроллере"**
*   **Что делать:** Удали конструктор, убери `final` у поля, поставь над полем `@Autowired`.
*   **Зачем:** Препод хочет проверить, знаешь ли ты, что это "плохой тон" (Field Injection) и почему (нельзя сделать поле `final`, сложнее писать Unit-тесты без Spring-контейнера).

**Задание 4: "Измени путь эндпоинта и добавь новый параметр"**
*   **Что делать:** В `NotificationController` измени `@GetMapping("/notify")` на `@GetMapping("/send")` и добавь `@RequestParam String priority`.
*   **Зачем:** Проверка навыков работы с Spring Web.

