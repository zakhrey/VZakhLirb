package com.zakhrey.vzakhlib.service;

import com.zakhrey.vzakhlib.model.BookDto;
import com.zakhrey.vzakhlib.model.PageResponse;
import com.zakhrey.vzakhlib.model.SeriesDto;
import com.zakhrey.vzakhlib.model.request.SeriesCreateRequest;
import com.zakhrey.vzakhlib.model.request.SeriesUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SeriesService {
    
    /**
     * Создать серию
     */
    SeriesDto createSeries(SeriesCreateRequest request);
    
    /**
     * Просмотреть список серий с пагинацией
     */
    PageResponse<SeriesDto> getAllSeries(Pageable pageable);
    
    /**
     * Изменить серию
     */
    SeriesDto updateSeries(UUID seriesId, SeriesUpdateRequest request);
    
    /**
     * Удалить серию
     */
    void deleteSeries(UUID seriesId);
    
    /**
     * Показать серию со всеми ее книгами с пагинацией
     */
    PageResponse<BookDto> getSeriesWithBooks(UUID seriesId, Pageable pageable);
    
    /**
     * Получить серию по ID
     */
    SeriesDto getSeriesById(UUID seriesId);
}