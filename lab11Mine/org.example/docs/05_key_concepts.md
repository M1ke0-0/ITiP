# Ключевые концепции

## Layered Architecture (Слоистая архитектура)
```
Client (Postman)
      ↓ HTTP запрос
Controller  — принимает запрос, возвращает ответ
      ↓
Service     — бизнес-логика
      ↓
Repository  — работа с БД
      ↓
Database (PostgreSQL)
```
Каждый слой знает только о соседнем — это упрощает тестирование и изменение кода.

---

## Spring Data JPA — Query Derivation
Spring автоматически строит SQL по имени метода:

| Метод | SQL |
|---|---|
| `findByStatus(status)` | `WHERE status = ?` |
| `findByStatusAndChannel(s, c)` | `WHERE status = ? AND channel = ?` |
| `findByStatusOrderByCreatedAtAsc(s)` | `WHERE status = ? ORDER BY created_at ASC` |
| `findAllByOrderByCreatedAtDesc()` | `ORDER BY created_at DESC` |

---

## @Query — ручные запросы

**JPQL** (Java Persistence Query Language) — работает с Java-объектами:
```java
@Query("select n from Notification n where n.recipient.id = :id and n.status = :status")
```

**Native SQL** — работает с таблицами и колонками БД:
```java
@Query(value = "select * from notifications where status = :status", nativeQuery = true)
```

---

## @Transactional
Гарантирует атомарность операций:
- Если метод завершился успешно → `COMMIT` (данные сохраняются)
- Если возникло исключение → `ROLLBACK` (все изменения отменяются)

```java
@Transactional
public Notification createNotification(NotificationDto request) {
    // если здесь упадёт исключение — save() будет откатан
}
```

---

## FetchType.LAZY
```java
@ManyToOne(fetch = FetchType.LAZY)
private User recipient;
```
`LAZY` — `User` загружается из БД только при обращении к полю `recipient`, а не сразу при загрузке `Notification`. Экономит лишние запросы к БД.

---

## Изменённые файлы в ходе лабы
| Файл | Что изменено |
|---|---|
| `model/entity/User.java` | Исправлен package |
| `model/entity/Notification.java` | Исправлен package и импорты |
| `model/dto/UserDto.java` | Добавлена валидация полей |
| `model/dto/NotificationDto.java` | Исправлены импорты |
| `model/enums/*.java` | Исправлен package |
| `repository/UserRepository.java` | Исправлены импорты |
| `repository/NotificationRepository.java` | Добавлены новые методы поиска |
| `service/UserService.java` | Добавлен package, исправлены импорты |
| `service/NotificationService.java` | Добавлен package, `@Transactional`, автоустановка `sentAt` |
| `controller/UserController.java` | Добавлен package, исправлены импорты |
| `controller/NotificationController.java` | Полностью переписан с CRUD |
| `pom.xml` | Добавлена конфигурация Lombok annotation processor |
| `application.properties` | Настройки подключения к PostgreSQL |
