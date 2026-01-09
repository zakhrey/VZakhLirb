package com.zakhrey.vzakhlib.repository;

import com.zakhrey.vzakhlib.entity.Genre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenreRepository extends CrudRepository<Genre, UUID> {
    
    // Найти жанр по названию (точное совпадение)
    Optional<Genre> findByName(String name);
    
    // Найти жанры по части названия
    List<Genre> findByNameContainingIgnoreCase(String namePart);
    
    // Получить жанр с книгами (жадная загрузка)
    @Query("SELECT g FROM Genre g LEFT JOIN FETCH g.books WHERE g.id = :id")
    Optional<Genre> findByIdWithBooks(@Param("id") UUID id);
    
    // Найти несколько жанров по списку ID
    List<Genre> findByIdIn(List<UUID> ids);
    
    // Проверить существование по названию
    boolean existsByName(String name);
}