package com.zakhrey.vzakhlib.repository;

import com.zakhrey.vzakhlib.entity.Language;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends CrudRepository<Book, UUID> {
    
    // Найти книгу по названию (точное совпадение)
    Optional<Book> findByName(String name);
    
    // Найти книги по части названия
    List<Book> findByNameContainingIgnoreCase(String namePart);
    
    // Найти книги по языку
    List<Book> findByLanguage(Language language);
    
    // Найти книги по серии
    List<Book> findBySeriesId(UUID seriesId);

    // Найти книги по жанру
    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.id = :genreId")
    List<Book> findByGenreId(@Param("genreId") UUID genreId);

    // Получить книгу с серией и жанрами (жадная загрузка)
    @Query("SELECT b FROM Book b " +
           "LEFT JOIN FETCH b.series " +
           "LEFT JOIN FETCH b.genres " +
           "WHERE b.id = :id")
    Optional<Book> findByIdWithSeriesAndGenres(@Param("id") UUID id);
    
    // Проверить существование по названию
    boolean existsByName(String name);
}