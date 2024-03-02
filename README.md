## :black_joker: Телеграм Бот с анекдотами на Spring Boot

- Реализован телеграм бот на базе [библиотеки Telegram-Bot-API](https://github.com/pengrad/java-telegram-bot-api/tree/master) который может вытаскивать случайный анекдот из базы данных.

- Реализован REST API с следующим функционалом:

```
GET /jokes - выдача всех шуток
GET /jokes/id - выдача шутки с определенным ID
DELETE /jokes/id - удаление шутки

С параметром "joke" принимающий строку:
POST /jokes - создание новой шутки
PUT /jokes/id - изменение шутки
```

Задание первой лабораторной работы по Java.
