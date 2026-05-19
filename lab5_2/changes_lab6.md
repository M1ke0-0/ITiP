# Изменения в проекте (Выполнение лабораторной работы №6)

В рамках лабораторной работы №6 были успешно реализованы все учебные и самостоятельные задания по тестированию веб-приложения на Spring Boot с использованием **JUnit 5** и **Mockito**. 

В связи с использованием **Spring Boot 4.0.5**, были применены современные стандарты тестирования (включая замену устаревшей `@MockBean` на `@MockitoBean` и использование новых пакетов автоконфигурации `@WebMvcTest` в Spring Boot 4.x). 

Также решена проблема интеграционного тестирования: подключена in-memory база данных **H2** для тестов и исправлена конфигурация сканирования пакетов, мешавшая изолированному тестированию веб-слоя.

---

## 🛠️ Системные изменения и оптимизации

### 1. Подключение H2-карты для тестов в `pom.xml`
Заменен веб-тестовый стартер на стандартный `spring-boot-starter-test`, добавлен специализированный `spring-boot-starter-webmvc-test` для веб-тестов и подключена in-memory база данных **H2** для тестовой области:
```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>test</scope>
		</dependency>
```

### 2. Добавлен `src/test/resources/application.properties`
Создана конфигурация для тестов, перенаправляющая JPA на базу данных H2 (что позволяет запускать `@SpringBootTest` и другие тесты без необходимости держать запущенным PostgreSQL локально):
```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### 3. Исправление `Application.java`
Удален неявный `@Import(AppConfig.class)` из главного класса приложения. Оригинальный `AppConfig` принудительно делал `@ComponentScan` всего проекта, что ломало изоляцию `@WebMvcTest` в тестах контроллеров, заставляя Spring Boot поднимать все тяжелые сервисы и репозитории в веб-тестах. Теперь приложение сканируется стандартным и оптимизированным образом.

---

## 🧪 Реализованные тесты (`src/test/java/spring_lab3_notifications/org/example`)

### 1. Базовые тесты JUnit 5 (Части 1, 2)

*   **`SimpleTest.java`** — Простейший арифметический тест (Часть 1).
*   **`AssertionsTest.java`** — Демонстрация стандартных проверок `assertEquals`, `assertNotNull`, `assertTrue`, `assertFalse` (Часть 2.1).
*   **`ExceptionTest.java`** — Проверка корректности выброса исключений через `assertThrows` (Часть 2.2).
*   **`LifecycleTest.java`** — Работа с аннотациями жизненного цикла `@BeforeEach` и `@AfterEach` (Часть 2.3).

---

### 2. Unit-тесты сервисного слоя с Mockito (Части 4, 5, 6, 7 & Самостоятельные задания)

#### `service/UserServiceTest.java`
Содержит тесты для `UserService` с использованием mock-репозитория `UserRepository` (Самостоятельные задания 1, 2, 6):
*   `shouldCreateUser()` — Тест создания пользователя (Часть 4).
*   `shouldGetUserById()` — **[Самостоятельное задание №1]** Проверка корректного получения пользователя по ID.
*   `shouldThrowExceptionWhenUserNotFound()` — Проверка выброса исключения при поиске несуществующего пользователя.
*   `shouldDeleteUser()` — **[Самостоятельные задания №2, №6]** Проверка удаления пользователя с верификацией вызова `userRepository.delete(...)` ровно один раз через `verify(..., times(1))`.

#### `service/UserServiceVerifyTest.java`
*   `shouldCallSaveOnRepository()` — Проверка вызова метода `save()` на уровне репозитория с помощью `verify` (Часть 5).

#### `service/NotificationServiceTest.java`
Тесты бизнес-логики уведомлений (Часть 6 & Самостоятельные задания 3, 4):
*   `shouldCreateNotification()` — Создание уведомления с подстановкой mock-пользователя (Часть 6).
*   `shouldGetNotificationById()` — **[Самостоятельное задание №3]** Положительный тест получения уведомления по ID.
*   `shouldThrowExceptionWhenNotificationNotFound()` — **[Самостоятельное задание №4]** Негативный тест поиска отсутствующего уведомления с проверкой выброса исключения.

#### `service/NotificationServiceExceptionTest.java`
*   `shouldThrowExceptionWhenUserNotFound()` — Тестирование ошибки при создании уведомления для несуществующего пользователя (Часть 7).

#### `service/AuthServiceTest.java`
**[Самостоятельное задание №5, №6]** Новый тестовый класс для `AuthService` из лабораторной работы по Spring Security:
*   `shouldRegisterUserSuccessfully()` — Регистрация обычного пользователя. Проверяет корректное шифрование пароля, установку роли `ROLE_USER` и сохранение с помощью `ArgumentCaptor` и `verify(..., times(1))`.
*   `shouldRegisterAdminSuccessfully()` — Регистрация администратора с ролью `ROLE_ADMIN`.
*   `shouldThrowExceptionWhenRegisteringExistingEmail()` — Проверка сценария выброса `EmailAlreadyExistsException`, если email уже зарегистрирован.

---

### 3. Специальные возможности Mockito

#### `SpyTest.java`
**[Самостоятельное задание №7]** Демонстрация работы с частичным мокированием шпионов (`spy`):
*   Частичное мокирование `ArrayList` с реальным изменением размера списка и верификацией вызова метода `.add()`.

---

### 4. Тестирование веб-слоя через `@WebMvcTest` (Часть 10 & Самостоятельные задания)

Для Spring Boot 4.0.5 использовались современные импорты аннотаций из пакета `org.springframework.boot.webmvc.test.autoconfigure.*`. Фильтры безопасности Spring Security были отключены через `@AutoConfigureMockMvc(addFilters = false)`, а все бины безопасности и зависимые сервисы были заменены на `@MockitoBean`.

#### `controller/UserControllerTest.java`
*   `shouldReturnOk()` — Веб-тест `UserController` для метода `/users/all` (Часть 10).

#### `controller/NotificationControllerTest.java`
**[Самостоятельное задание №8]** Новый класс для веб-тестирования `NotificationController`:
*   `shouldReturnOkForGetAllNotifications()` — Симуляция GET-запроса `/notifications/all` с проверкой HTTP-статуса `200 OK`.

---

## 📈 Результат выполнения тестов

Все **21 тест** компилируются, запускаются и выполняются успешно:
```bash
[INFO] Running spring_lab3_notifications.org.example.SimpleTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running spring_lab3_notifications.org.example.AssertionsTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running spring_lab3_notifications.org.example.ExceptionTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running spring_lab3_notifications.org.example.LifecycleTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running spring_lab3_notifications.org.example.SpyTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running spring_lab3_notifications.org.example.service.UserServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running spring_lab3_notifications.org.example.service.UserServiceVerifyTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running spring_lab3_notifications.org.example.service.NotificationServiceTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running spring_lab3_notifications.org.example.service.NotificationServiceExceptionTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running spring_lab3_notifications.org.example.service.AuthServiceTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running spring_lab3_notifications.org.example.controller.UserControllerTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running spring_lab3_notifications.org.example.controller.NotificationControllerTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] Tests run: 21, Failures: 0, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
