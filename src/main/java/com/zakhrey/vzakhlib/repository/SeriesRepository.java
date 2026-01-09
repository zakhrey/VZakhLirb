package com.zakhrey.vzakhlib.repository;

import com.zakhrey.vzakhlib.entity.Series;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeriesRepository extends CrudRepository<Series, UUID> {

    // Найти серию по названию (точное совпадение)
    Optional<Series> findByName(String name);

    // Найти серии по части названия
    List<Series> findByNameContainingIgnoreCase(String namePart);

    // Получить серию с книгами (жадная загрузка)
    @Query("SELECT s FROM Series s LEFT JOIN FETCH s.books WHERE s.id = :id")
    Optional<Series> findByIdWithBooks(@Param("id") UUID id);

    // Проверить существование по названию
    boolean existsByName(String name);
}