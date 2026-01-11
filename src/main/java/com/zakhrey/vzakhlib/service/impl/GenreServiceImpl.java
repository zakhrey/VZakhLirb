package com.zakhrey.vzakhlib.service.impl;

import com.zakhrey.vzakhlib.entity.Book;
import com.zakhrey.vzakhlib.entity.Genre;
import com.zakhrey.vzakhlib.exception.ResourceAlreadyExistsException;
import com.zakhrey.vzakhlib.exception.ResourceNotFoundException;
import com.zakhrey.vzakhlib.mapper.EntityMapper;
import com.zakhrey.vzakhlib.model.BookDto;
import com.zakhrey.vzakhlib.model.GenreDto;
import com.zakhrey.vzakhlib.model.PageResponse;
import com.zakhrey.vzakhlib.model.request.GenreCreateRequest;
import com.zakhrey.vzakhlib.model.request.GenreUpdateRequest;
import com.zakhrey.vzakhlib.repository.BookRepository;
import com.zakhrey.vzakhlib.repository.GenreRepository;
import com.zakhrey.vzakhlib.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    
    @Override
    @Transactional
    public GenreDto createGenre(GenreCreateRequest request) {
        log.info("Создание жанра с названием: {}", request.getName());
        
        // Проверка на существование жанра с таким же названием
        if (genreRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException(
                String.format("Жанр с названием '%s' уже существует", request.getName())
            );
        }
        
        // Создание нового жанра
        Genre genre = Genre.builder()
            .name(request.getName())
            .description(request.getDescription())
            .build();
        
        Genre savedGenre = genreRepository.save(genre);
        log.info("Жанр создан с ID: {}", savedGenre.getId());
        
        return EntityMapper.toDto(savedGenre);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<GenreDto> getAllGenres(Pageable pageable) {
        log.info("Получение списка жанров с пагинацией: {}", pageable);
        
        Page<Genre> genrePage = genreRepository.findAll(pageable);
        Page<GenreDto> dtoPage = genrePage.map(EntityMapper::toDto);
        
        return PageResponse.of(dtoPage);
    }

    @Override
    @Transactional
    public GenreDto updateGenre(UUID genreId, GenreUpdateRequest request) {
        log.info("Обновление жанра с ID: {}", genreId);
        
        // Поиск жанра
        Genre genre = genreRepository.findById(genreId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Жанр с ID '%s' не найден", genreId)
            ));
        
        // Проверка уникальности нового названия (если оно изменилось)
        if (request.getName() != null && !request.getName().equals(genre.getName())) {
            if (genreRepository.existsByName(request.getName())) {
                throw new ResourceAlreadyExistsException(
                    String.format("Жанр с названием '%s' уже существует", request.getName())
                );
            }
            genre.setName(request.getName());
        }
        
        // Обновление описания (если предоставлено)
        if (request.getDescription() != null) {
            genre.setDescription(request.getDescription());
        }
        
        Genre updatedGenre = genreRepository.save(genre);
        log.info("Жанр с ID {} обновлен", genreId);
        
        return EntityMapper.toDto(updatedGenre);
    }

    @Override
    @Transactional
    public void deleteGenre(UUID genreId) {
        log.info("Удаление жанра с ID: {}", genreId);
        
        // Проверка существования жанра
        if (!genreRepository.existsById(genreId)) {
            throw new ResourceNotFoundException(
                String.format("Жанр с ID '%s' не найден", genreId)
            );
        }
        
        genreRepository.deleteById(genreId);
        log.info("Жанр с ID {} удален", genreId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BookDto> getBooksByGenre(UUID genreId, Pageable pageable) {
        log.info("Получение книг жанра с ID: {}", genreId);
        
        // Проверка существования жанра
        if (!genreRepository.existsById(genreId)) {
            throw new ResourceNotFoundException(
                String.format("Жанр с ID '%s' не найден", genreId)
            );
        }
        
        // Получение книг с пагинацией
        Page<Book> bookPage = bookRepository.findByGenreId(genreId, pageable);
        Page<BookDto> dtoPage = bookPage.map(EntityMapper::toDto);
        
        return PageResponse.of(dtoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public GenreDto getGenreById(UUID genreId) {
        log.info("Получение жанра с ID: {}", genreId);
        
        Genre genre = genreRepository.findById(genreId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Жанр с ID '%s' не найден", genreId)
            ));
        
        return EntityMapper.toDto(genre);
    }
}