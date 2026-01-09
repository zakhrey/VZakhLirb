# VZakhLirb
VZakhLirb library applocation

Нефункциональные требования: Java 17, Spring Boot, Spring Data JPA, Postgres, Lombok

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
    
