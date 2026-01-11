package com.zakhrey.vzakhlib.repository;

import com.zakhrey.vzakhlib.entity.Book;
import com.zakhrey.vzakhlib.entity.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends CrudRepository<Book, UUID> {

    Page<Book> findAll(Pageable pageable);
    
    // Найти книги по серии
    List<Book> findBySeriesId(UUID seriesId);

    Page<Book> findBySeriesId(UUID seriesId, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.id = :genreId")
    Page<Book> findByGenreId(@Param("genreId") UUID genreId, Pageable pageable);

    // Получить книгу с серией и жанрами (жадная загрузка)
    @Query("SELECT b FROM Book b " +
           "LEFT JOIN FETCH b.series " +
           "LEFT JOIN FETCH b.genres " +
           "WHERE b.id = :id")
    Optional<Book> findByIdWithSeriesAndGenres(@Param("id") UUID id);
    
    // Проверить существование по названию
    boolean existsByName(String name);

    @Query("SELECT b FROM Book b WHERE " +
            "(:name IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:language IS NULL OR b.language = :language) AND " +
            "(:seriesId IS NULL OR b.series.id = :seriesId)")
    Page<Book> searchBooks(@Param("name") String name,
                           @Param("language") Language language,
                           @Param("seriesId") UUID seriesId,
                           Pageable pageable);
}