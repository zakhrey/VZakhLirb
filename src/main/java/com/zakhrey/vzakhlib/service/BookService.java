package com.zakhrey.vzakhlib.service;

import com.zakhrey.vzakhlib.model.BookDto;
import com.zakhrey.vzakhlib.model.PageResponse;
import com.zakhrey.vzakhlib.model.request.BookCreateRequest;
import com.zakhrey.vzakhlib.model.request.BookUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BookService {

    /**
     * Создать новую книгу
     */
    BookDto createBook(BookCreateRequest request);

    /**
     * Просмотреть список книг с пагинацией
     */
    PageResponse<BookDto> getAllBooks(Pageable pageable);

    /**
     * Привязать книгу к серии
     */
    BookDto attachBookToSeries(UUID bookId, UUID seriesId);

    /**
     * Отвязать книгу от серии
     */
    BookDto detachBookFromSeries(UUID bookId);

    /**
     * Добавить книге жанр
     */
    BookDto addGenreToBook(UUID bookId, UUID genreId);

    /**
     * Удалить жанр у книги
     */
    BookDto removeGenreFromBook(UUID bookId, UUID genreId);

    /**
     * Изменить книгу
     */
    BookDto updateBook(UUID bookId, BookUpdateRequest request);

    /**
     * Удалить книгу
     */
    void deleteBook(UUID bookId);

    /**
     * Получить книгу по ID
     */
    BookDto getBookById(UUID bookId);

    /**
     * Поиск книг по различным критериям
     */
    PageResponse<BookDto> searchBooks(String name,
                                      String language,
                                      UUID seriesId,
                                      Pageable pageable);
}