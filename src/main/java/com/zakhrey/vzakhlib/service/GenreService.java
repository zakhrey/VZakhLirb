package com.zakhrey.vzakhlib.service;

import com.zakhrey.vzakhlib.model.BookDto;
import com.zakhrey.vzakhlib.model.GenreDto;
import com.zakhrey.vzakhlib.model.PageResponse;
import com.zakhrey.vzakhlib.model.request.GenreCreateRequest;
import com.zakhrey.vzakhlib.model.request.GenreUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GenreService {
    
    /**
     * Добавление жанра
     */
    GenreDto createGenre(GenreCreateRequest request);
    
    /**
     * Просмотр списка жанров с пагинацией
     */
    PageResponse<GenreDto> getAllGenres(Pageable pageable);
    
    /**
     * Изменение жанра
     */
    GenreDto updateGenre(UUID genreId, GenreUpdateRequest request);
    
    /**
     * Удаление жанра
     */
    void deleteGenre(UUID genreId);
    
    /**
     * Найти все книги одного жанра с пагинацией
     */
    PageResponse<BookDto> getBooksByGenre(UUID genreId, Pageable pageable);
    
    /**
     * Получить жанр по ID
     */
    GenreDto getGenreById(UUID genreId);
}