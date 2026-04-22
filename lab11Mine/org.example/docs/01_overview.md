# Лабораторная работа 11 — Spring Data JPA

## Цель
Изучить работу со Spring Data JPA: создание сущностей, репозиториев, сервисов и REST-контроллеров для работы с базой данных PostgreSQL.

## Что реализовано
- Подключение Spring Boot приложения к PostgreSQL
- Автоматическое создание таблиц через Hibernate (`ddl-auto=update`)
- CRUD-операции для сущностей `User` и `Notification`
- Валидация входных данных через Bean Validation (`@Valid`, `@NotBlank`, `@Email`, `@Pattern`)
- Транзакционность через `@Transactional`
- Расширенные методы репозитория: по нескольким параметрам, с сортировкой, через `@Query`

## Структура проекта
```
src/main/java/spring_lab3_notifications/org/example/
├── Application.java               — точка входа
├── config/
│   └── AppConfig.java             — конфигурация Spring
├── controller/
│   ├── UserController.java        — REST эндпоинты для пользователей
│   └── NotificationController.java — REST эндпоинты для уведомлений
├── service/
│   ├── UserService.java           — бизнес-логика пользователей
│   └── NotificationService.java   — бизнес-логика уведомлений
├── repository/
│   ├── UserRepository.java        — доступ к таблице users
│   └── NotificationRepository.java — доступ к таблице notifications
└── model/
    ├── entity/
    │   ├── User.java              — сущность пользователя (таблица users)
    │   └── Notification.java      — сущность уведомления (таблица notifications)
    ├── dto/
    │   ├── UserDto.java           — DTO для пользователя
    │   └── NotificationDto.java   — DTO для уведомления
    └── enums/
        ├── NotificationChannel.java — EMAIL, SMS, PUSH, TELEGRAM
        └── NotificationStatus.java  — CREATED, SENT, FAILED
```
