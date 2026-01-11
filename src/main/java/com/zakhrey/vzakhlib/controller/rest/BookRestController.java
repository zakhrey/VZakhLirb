package com.zakhrey.vzakhlib.controller.rest;

import com.zakhrey.vzakhlib.model.BookDto;
import com.zakhrey.vzakhlib.model.PageResponse;
import com.zakhrey.vzakhlib.model.request.BookCreateRequest;
import com.zakhrey.vzakhlib.model.request.BookUpdateRequest;
import com.zakhrey.vzakhlib.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
public class BookRestController {

    private final BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Создать новую книгу
     */
    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody BookCreateRequest request) {
        BookDto createdBook = bookService.createBook(request);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdBook.getId())
                .toUri();
        
        return ResponseEntity
                .created(location)
                .body(createdBook);
    }

    /**
     * Получить список всех книг с пагинацией
     */
    @GetMapping
    public ResponseEntity<PageResponse<BookDto>> getAllBooks(
            @PageableDefault(size = 20, sort = "title") Pageable pageable) {
        PageResponse<BookDto> booksPage = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(booksPage);
    }

    /**
     * Получить книгу по ID
     */
    @GetMapping("/{bookId}")
    public ResponseEntity<BookDto> getBookById(@PathVariable UUID bookId) {
        BookDto book = bookService.getBookById(bookId);
        return ResponseEntity.ok(book);
    }

    /**
     * Обновить книгу
     */
    @PutMapping("/{bookId}")
    public ResponseEntity<BookDto> updateBook(
            @PathVariable UUID bookId,
            @RequestBody BookUpdateRequest request) {
        BookDto updatedBook = bookService.updateBook(bookId, request);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Удалить книгу
     */
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Привязать книгу к серии
     */
    @PostMapping("/{bookId}/series/{seriesId}")
    public ResponseEntity<BookDto> attachBookToSeries(
            @PathVariable UUID bookId,
            @PathVariable UUID seriesId) {
        BookDto updatedBook = bookService.attachBookToSeries(bookId, seriesId);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Отвязать книгу от серии
     */
    @DeleteMapping("/{bookId}/series")
    public ResponseEntity<BookDto> detachBookFromSeries(@PathVariable UUID bookId) {
        BookDto updatedBook = bookService.detachBookFromSeries(bookId);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Добавить жанр к книге
     */
    @PostMapping("/{bookId}/genres/{genreId}")
    public ResponseEntity<BookDto> addGenreToBook(
            @PathVariable UUID bookId,
            @PathVariable UUID genreId) {
        BookDto updatedBook = bookService.addGenreToBook(bookId, genreId);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Удалить жанр у книги
     */
    @DeleteMapping("/{bookId}/genres/{genreId}")
    public ResponseEntity<BookDto> removeGenreFromBook(
            @PathVariable UUID bookId,
            @PathVariable UUID genreId) {
        BookDto updatedBook = bookService.removeGenreFromBook(bookId, genreId);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Поиск книг по различным критериям
     */
    @GetMapping("/search")
    public ResponseEntity<PageResponse<BookDto>> searchBooks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) UUID seriesId,
            @PageableDefault(size = 20, sort = "title") Pageable pageable) {
        
        PageResponse<BookDto> searchResult = bookService.searchBooks(name, language, seriesId, pageable);
        return ResponseEntity.ok(searchResult);
    }

    /**
     * Частичное обновление книги (PATCH)
     */
    @PatchMapping("/{bookId}")
    public ResponseEntity<BookDto> partialUpdateBook(
            @PathVariable UUID bookId,
            @RequestBody BookUpdateRequest request) {
        BookDto updatedBook = bookService.updateBook(bookId, request);
        return ResponseEntity.ok(updatedBook);
    }
}