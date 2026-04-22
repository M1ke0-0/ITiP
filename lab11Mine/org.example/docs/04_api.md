# API эндпоинты

Base URL: `http://localhost:8080`

---

## Пользователи `/users`

### POST /users/add — создать пользователя
**Body:**
```json
{
  "name": "Иван Иванов",
  "email": "ivan@example.com",
  "phone": "+79990001122",
  "deviceToken": "device-token-123",
  "telegramChatId": "123456789"
}
```

### GET /users/all — получить всех пользователей

### GET /users/{id} — получить по ID

### PUT /users/{id} — обновить пользователя
**Body:** те же поля что и POST

### DELETE /users/{id} — удалить пользователя

---

## Уведомления `/notifications`

### POST /notifications/add — создать уведомление
**Body:**
```json
{
  "title": "Напоминание",
  "message": "Завтра состоится занятие по Spring Data",
  "channel": "EMAIL",
  "recipientId": 1
}
```
Доступные каналы: `EMAIL`, `SMS`, `PUSH`, `TELEGRAM`

### GET /notifications/all — получить все уведомления

### GET /notifications/{id} — получить по ID

### PUT /notifications/{id} — обновить уведомление
**Body:**
```json
{
  "title": "Обновлено",
  "message": "Новый текст",
  "channel": "SMS",
  "status": "SENT",
  "recipientId": 1
}
```
При `status: "SENT"` автоматически устанавливается `sentAt`.

### DELETE /notifications/{id} — удалить уведомление

### GET /notifications/status/{status} — фильтр по статусу
Пример: `/notifications/status/CREATED`

### GET /notifications/channel/{channel} — фильтр по каналу
Пример: `/notifications/channel/EMAIL`

### GET /notifications/recipient/{recipientId} — уведомления пользователя
Пример: `/notifications/recipient/1`
