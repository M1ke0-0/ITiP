# Шпаргалка для защиты Лабораторной №6 (Тестирование в Spring Boot)

## 📌 Базовые понятия
- **Unit-тест** — проверка одного изолированного класса/метода.
- **Integration-тест** — проверка взаимодействия компонентов (часто с БД и поднятием Spring-контекста).
- **Mock** — фиктивный объект, заменяющий реальную зависимость.
- **Stub** — заданное поведение для mock-объекта (возврат конкретного значения).
- **Spy** — оборачивает реальный объект, сохраняя его логику, но позволяя следить за вызовами.

## 🟢 JUnit 5 (Аннотации и Assertions)
- `@Test` — помечает метод как тестовый.
- `@BeforeEach` / `@AfterEach` — выполняется до/после **каждого** метода.
- `@BeforeAll` / `@AfterAll` — выполняется один раз до/после **всех** тестов в классе (метод должен быть `static`).

**Популярные Assertions (проверки):**
```java
assertEquals(expected, actual);    // Проверка равенства
assertTrue(condition);             // Проверка на true
assertNotNull(object);             // Проверка, что объект не null
assertThrows(Exception.class, () -> { ... }); // Проверка выброса исключения
```

## 🟡 Mockito (Работа с Mock-объектами)
- `@ExtendWith(MockitoExtension.class)` — подключает Mockito к классу тестов.
- `@Mock` — создает mock-заглушку.
- `@InjectMocks` — создает реальный объект и внедряет в него все `@Mock`.

**Настройка поведения (Stubbing):**
```java
// Когда вызовут findById(1), вернуть Optional.of(user)
when(repository.findById(1L)).thenReturn(Optional.of(user));

// Когда вызовут с ЛЮБЫМ ID, выкинуть исключение
when(repository.findById(any())).thenThrow(new RuntimeException());
```

**Проверка вызовов (Verifying):**
```java
// Проверка, что save() был вызван с любым объектом User
verify(repository).save(any(User.class));

// Проверка, что метод deleteById(1) был вызван ровно 1 раз
verify(repository, times(1)).deleteById(1L);

// Проверка, что метод никогда не вызывался
verify(repository, never()).deleteAll();
```

## 🔵 Spring Boot Тесты (Веб-слой)
- `@WebMvcTest(Controller.class)` — поднимает только веб-слой (без сервисов и БД).
- `@MockBean` — добавляет mock-объект прямо в ApplicationContext Spring (чтобы контроллер мог его использовать).
- `MockMvc` — инструмент для симуляции HTTP-запросов.

**Пример запроса через MockMvc:**
```java
mockMvc.perform(get("/api/users/1"))
       .andExpect(status().isOk())            // Ожидаем статус 200 OK
       .andExpect(jsonPath("$.name").value("Иван")); // Ожидаем JSON { "name": "Иван" }
```

## 🚀 Алгоритм написания Unit-теста для Service
1. Пометить класс `@ExtendWith(MockitoExtension.class)`.
2. Создать mock-репозитории (`@Mock`).
3. Создать тестируемый сервис (`@InjectMocks`).
4. Подготовить входные данные (DTO, Entities).
5. Задать поведение мокам через `when(...).thenReturn(...)`.
6. Вызвать метод сервиса.
7. Проверить результат через `assertEquals() / assertNotNull()`.
8. Опционально: проверить вызовы моков через `verify()`.
