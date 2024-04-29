## :black_joker: Телеграм Бот с анекдотами на Spring Boot :black_joker:

Реализован телеграм бот (на базе [библиотеки Telegram-Bot-API](https://github.com/pengrad/java-telegram-bot-api/tree/master)) с помощью Telegram API, Spring Boot, JPA, Spring Security.

Реализован REST API с следующим функционалом:


*Шутки*
```
GET /jokes?page={page} - выдача всех шуток, реализована пагинация, вместо page принимается параметр страницы.
GET /jokes/{id} - выдача шутки с определенным ID
GET jokes/topJokes - выдача 5 самых популярных шуток (по количеству вызовов в API и боте)
DELETE /jokes/{id} - удаление шутки
```
Запросы которые должны содержать в теле JSON такого формата:
```
{
    "joke": string
}
```
```
POST /jokes - создание новой шутки
PUT /jokes/{id} - изменение шутки
```
*Управление доступом*
```
GET /users/getAllRoles - возвращает все доступные роли
GET /users/getAllUsers?page={page} - возвращает список пользователей
PUT /users/addRole?userId={id}&roleId={id} - Добавить пользователю роль
PUT /users/removeRole?userId={id}&roleId={id} - Удалить роль у пользователя
DELETE /users/deleteUser?userId={id} - Удалить пользователя
```
Для запроса ``` POST /users/register ``` небходимо в теле запроса иметь JSON следующей формы:
```
{
    "username": string,
    "password": string
}
```
Задание лабораторных работ по Java, Delmark (TM).
