package com.zakhrey.vzakhlib.service.impl;

import com.zakhrey.vzakhlib.model.BookDto;
import com.zakhrey.vzakhlib.model.request.BookCreateRequest;
import com.zakhrey.vzakhlib.model.request.BookUpdateRequest;
import com.zakhrey.vzakhlib.model.PageResponse;
import com.zakhrey.vzakhlib.entity.Book;
import com.zakhrey.vzakhlib.entity.Genre;
import com.zakhrey.vzakhlib.entity.Series;
import com.zakhrey.vzakhlib.exception.ResourceAlreadyExistsException;
import com.zakhrey.vzakhlib.exception.ResourceNotFoundException;
import com.zakhrey.vzakhlib.mapper.EntityMapper;
import com.zakhrey.vzakhlib.entity.Language;
import com.zakhrey.vzakhlib.repository.BookRepository;
import com.zakhrey.vzakhlib.repository.GenreRepository;
import com.zakhrey.vzakhlib.repository.SeriesRepository;
import com.zakhrey.vzakhlib.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    
    private final BookRepository bookRepository;
    private final SeriesRepository seriesRepository;
    private final GenreRepository genreRepository;

    @Override
    @Transactional
    public BookDto createBook(BookCreateRequest request) {
        log.info("Создание книги с названием: {}", request.getName());
        
        // Проверка на существование книги с таким же названием
        if (bookRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException(
                String.format("Книга с названием '%s' уже существует", request.getName())
            );
        }
        
        // Создание новой книги
        Book book = Book.builder()
            .name(request.getName())
            .description(request.getDescription())
            .language(Language.valueOf(request.getLanguage().toUpperCase()))
            .fileLink(request.getFileLink())
            .genres(new HashSet<>())
            .build();
        
        // Привязка к серии (если указана)
        if (request.getSeriesId() != null) {
            Series series = seriesRepository.findById(request.getSeriesId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Серия с ID '%s' не найдена", request.getSeriesId())
                ));
            book.setSeries(series);
        }
        
        // Добавление жанров (если указаны)
        if (request.getGenreIds() != null && !request.getGenreIds().isEmpty()) {
            request.getGenreIds().forEach(genreId -> {
                Genre genre = genreRepository.findById(genreId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Жанр с ID '%s' не найден", genreId)
                    ));
                book.getGenres().add(genre);
            });
        }
        
        Book savedBook = bookRepository.save(book);
        log.info("Книга создана с ID: {}", savedBook.getId());
        
        return EntityMapper.toDto(savedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BookDto> getAllBooks(Pageable pageable) {
        log.info("Получение списка книг с пагинацией: {}", pageable);
        
        Page<Book> bookPage = bookRepository.findAll(pageable);
        Page<BookDto> dtoPage = bookPage.map(EntityMapper::toDto);
        
        return PageResponse.of(dtoPage);
    }

    @Override
    @Transactional
    public BookDto attachBookToSeries(UUID bookId, UUID seriesId) {
        log.info("Привязка книги с ID {} к серии с ID {}", bookId, seriesId);
        
        // Поиск книги
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Книга с ID '%s' не найдена", bookId)
            ));
        
        // Поиск серии
        Series series = seriesRepository.findById(seriesId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Серия с ID '%s' не найдена", seriesId)
            ));
        
        book.setSeries(series);
        Book updatedBook = bookRepository.save(book);
        log.info("Книга с ID {} привязана к серии с ID {}", bookId, seriesId);
        
        return EntityMapper.toDto(updatedBook);
    }

    @Override
    @Transactional
    public BookDto detachBookFromSeries(UUID bookId) {
        log.info("Отвязка книги с ID {} от серии", bookId);
        
        // Поиск книги
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Книга с ID '%s' не найдена", bookId)
            ));
        
        book.setSeries(null);
        Book updatedBook = bookRepository.save(book);
        log.info("Книга с ID {} отвязана от серии", bookId);
        
        return EntityMapper.toDto(updatedBook);
    }

    @Override
    @Transactional
    public BookDto addGenreToBook(UUID bookId, UUID genreId) {
        log.info("Добавление жанра с ID {} к книге с ID {}", genreId, bookId);
        
        // Поиск книги с жанрами
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Книга с ID '%s' не найдена", bookId)
            ));
        
        // Поиск жанра
        Genre genre = genreRepository.findById(genreId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Жанр с ID '%s' не найден", genreId)
            ));
        
        // Инициализация коллекции, если необходимо
        if (book.getGenres() == null) {
            book.setGenres(new HashSet<>());
        }
        
        // Добавление жанра
        book.getGenres().add(genre);
        Book updatedBook = bookRepository.save(book);
        log.info("Жанр с ID {} добавлен к книге с ID {}", genreId, bookId);
        
        return EntityMapper.toDto(updatedBook);
    }

    @Override
    @Transactional
    public BookDto removeGenreFromBook(UUID bookId, UUID genreId) {
        log.info("Удаление жанра с ID {} у книги с ID {}", genreId, bookId);
        
        // Поиск книги с жанрами
        Book book = bookRepository.findByIdWithSeriesAndGenres(bookId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Книга с ID '%s' не найдена", bookId)
            ));
        
        // Удаление жанра
        if (book.getGenres() != null) {
            book.getGenres().removeIf(genre -> genre.getId().equals(genreId));
        }
        
        Book updatedBook = bookRepository.save(book);
        log.info("Жанр с ID {} удален у книги с ID {}", genreId, bookId);
        
        return EntityMapper.toDto(updatedBook);
    }

    @Override
    @Transactional
    public BookDto updateBook(UUID bookId, BookUpdateRequest request) {
        log.info("Обновление книги с ID: {}", bookId);
        
        // Поиск книги
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Книга с ID '%s' не найдена", bookId)
            ));
        
        // Проверка уникальности нового названия (если оно изменилось)
        if (request.getName() != null && !request.getName().equals(book.getName())) {
            if (bookRepository.existsByName(request.getName())) {
                throw new ResourceAlreadyExistsException(
                    String.format("Книга с названием '%s' уже существует", request.getName())
                );
            }
            book.setName(request.getName());
        }
        
        // Обновление полей
        if (request.getDescription() != null) {
            book.setDescription(request.getDescription());
        }
        
        if (request.getLanguage() != null) {
            book.setLanguage(Language.valueOf(request.getLanguage().toUpperCase()));
        }
        
        if (request.getFileLink() != null) {
            book.setFileLink(request.getFileLink());
        }
        
        Book updatedBook = bookRepository.save(book);
        log.info("Книга с ID {} обновлена", bookId);
        
        return EntityMapper.toDto(updatedBook);
    }

    @Override
    @Transactional
    public void deleteBook(UUID bookId) {
        log.info("Удаление книги с ID: {}", bookId);
        
        // Проверка существования книги
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException(
                String.format("Книга с ID '%s' не найдена", bookId)
            );
        }
        
        bookRepository.deleteById(bookId);
        log.info("Книга с ID {} удалена", bookId);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto getBookById(UUID bookId) {
        log.info("Получение книги с ID: {}", bookId);
        
        Book book = bookRepository.findByIdWithSeriesAndGenres(bookId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Книга с ID '%s' не найдена", bookId)
            ));
        
        return EntityMapper.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BookDto> searchBooks(String name, String language, UUID seriesId, Pageable pageable) {
        log.info("Поиск книг с параметрами: name={}, language={}, seriesId={}", 
                 name, language, seriesId);
        
        Language lang = null;
        if (language != null && !language.isEmpty()) {
            try {
                lang = Language.valueOf(language.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Некорректный язык: {}", language);
            }
        }
        
        Page<Book> bookPage = bookRepository.searchBooks(name, lang, seriesId, pageable);
        Page<BookDto> dtoPage = bookPage.map(EntityMapper::toDto);
        
        return PageResponse.of(dtoPage);
    }
}