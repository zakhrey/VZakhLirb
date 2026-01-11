package com.zakhrey.vzakhlib.service.impl;

import com.zakhrey.vzakhlib.entity.Book;
import com.zakhrey.vzakhlib.entity.Series;
import com.zakhrey.vzakhlib.exception.ResourceAlreadyExistsException;
import com.zakhrey.vzakhlib.exception.ResourceNotFoundException;
import com.zakhrey.vzakhlib.mapper.EntityMapper;
import com.zakhrey.vzakhlib.model.BookDto;
import com.zakhrey.vzakhlib.model.PageResponse;
import com.zakhrey.vzakhlib.model.SeriesDto;
import com.zakhrey.vzakhlib.model.request.SeriesCreateRequest;
import com.zakhrey.vzakhlib.model.request.SeriesUpdateRequest;
import com.zakhrey.vzakhlib.repository.BookRepository;
import com.zakhrey.vzakhlib.repository.SeriesRepository;
import com.zakhrey.vzakhlib.service.SeriesService;
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
public class SeriesServiceImpl implements SeriesService {
    
    private final SeriesRepository seriesRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public SeriesDto createSeries(SeriesCreateRequest request) {
        log.info("Создание серии с названием: {}", request.getName());
        
        // Проверка на существование серии с таким же названием
        if (seriesRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException(
                String.format("Серия с названием '%s' уже существует", request.getName())
            );
        }
        
        // Создание новой серии
        Series series = Series.builder()
            .name(request.getName())
            .description(request.getDescription())
            .build();
        
        Series savedSeries = seriesRepository.save(series);
        log.info("Серия создана с ID: {}", savedSeries.getId());
        
        return EntityMapper.toDto(savedSeries);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<SeriesDto> getAllSeries(Pageable pageable) {
        log.info("Получение списка серий с пагинацией: {}", pageable);
        
        Page<Series> seriesPage = seriesRepository.findAll(pageable);
        Page<SeriesDto> dtoPage = seriesPage.map(EntityMapper::toDto);
        
        return PageResponse.of(dtoPage);
    }

    @Override
    @Transactional
    public SeriesDto updateSeries(UUID seriesId, SeriesUpdateRequest request) {
        log.info("Обновление серии с ID: {}", seriesId);
        
        // Поиск серии
        Series series = seriesRepository.findById(seriesId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Серия с ID '%s' не найден", seriesId)
            ));
        
        // Проверка уникальности нового названия (если оно изменилось)
        if (request.getName() != null && !request.getName().equals(series.getName())) {
            if (seriesRepository.existsByName(request.getName())) {
                throw new ResourceAlreadyExistsException(
                    String.format("Серия с названием '%s' уже существует", request.getName())
                );
            }
            series.setName(request.getName());
        }
        
        // Обновление описания (если предоставлено)
        if (request.getDescription() != null) {
            series.setDescription(request.getDescription());
        }
        
        Series updatedSeries = seriesRepository.save(series);
        log.info("Серия с ID {} обновлена", seriesId);
        
        return EntityMapper.toDto(updatedSeries);
    }

    @Override
    @Transactional
    public void deleteSeries(UUID seriesId) {
        log.info("Удаление серии с ID: {}", seriesId);
        
        // Проверка существования серии
        Series series = seriesRepository.findById(seriesId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Серия с ID '%s' не найдена", seriesId)
            ));
        
        // Удаление всех книг из серии
        bookRepository.findBySeriesId(seriesId).forEach(book -> {
            book.setSeries(null);
            bookRepository.save(book);
        });
        
        seriesRepository.delete(series);
        log.info("Серия с ID {} удалена", seriesId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<BookDto> getSeriesWithBooks(UUID seriesId, Pageable pageable) {
        log.info("Получение серии с ID {} и её книг", seriesId);
        
        // Проверка существования серии
        if (!seriesRepository.existsById(seriesId)) {
            throw new ResourceNotFoundException(
                String.format("Серия с ID '%s' не найдена", seriesId)
            );
        }
        
        // Получение книг серии с пагинацией
        Page<Book> bookPage = bookRepository.findBySeriesId(seriesId, pageable);
        Page<BookDto> dtoPage = bookPage.map(EntityMapper::toDto);
        
        return PageResponse.of(dtoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public SeriesDto getSeriesById(UUID seriesId) {
        log.info("Получение серии с ID: {}", seriesId);
        
        Series series = seriesRepository.findById(seriesId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Серия с ID '%s' не найдена", seriesId)
            ));
        
        return EntityMapper.toDto(series);
    }
}