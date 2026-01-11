# VZakhLirb
VZakhLirb library applocation

Нефункциональные требования: Java 17, Spring Boot, Spring Data JPA, Postgres, Lombok

## Task 2
- настргоить подключния к базе данных postgres
- настроить модель данных:
  - создать модель книг book
  - создать модель серий книг series
  - создать модель жанров книг genre
- создать репозитории к моделям



Описание моделей
series
    id: uuid,
    name: varchar(512),
    description: varchar(4096),
    create_ts: timestamp,
    update_ts: timestamp

genre
    id: uuid,
    name: varchar(512),
    description: varchar(4096),
    create_ts: timestamp,
    update_ts: timestamp

book
    id: uuid,
    name: varchar(512),
    description: varchar(4096),
    series_id: uuid,
    language(rus, eng),
    file_link: varchar(512),
    create_ts: timestamp,
    update_ts: timestamp


genre_for_book
    genre_id: uuid,
    book_id:  uuid

## Task 3
Написать сервисный слой к репозиториям

Сервис жанров:
- добавление жанра
  - просмотреть список жанров с пагинацией
  - изменение жанра
  - удаление жанра
  - найти все книги одного жанра с пагинацией

Сервис Серий:
- создать серию
  - просмотреть список серий с пагинацией
  - изменить серию
  - удалить серию
  - показать серию со всеми ее книгами с пагинацией

Сервис книг
- создать новую книгу
  - просмотреть список книг с пагинацией
  - привязать книгу к серии
  - добавить книге жанр
  - изменить книгу
  - удалить книгу

- Написать REST контроллеры к сервисам.

