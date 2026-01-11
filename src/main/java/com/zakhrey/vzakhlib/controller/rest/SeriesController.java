package com.zakhrey.vzakhlib.controller.rest;

import com.zakhrey.vzakhlib.model.BookDto;
import com.zakhrey.vzakhlib.model.PageResponse;
import com.zakhrey.vzakhlib.model.SeriesDto;
import com.zakhrey.vzakhlib.model.request.SeriesCreateRequest;
import com.zakhrey.vzakhlib.model.request.SeriesUpdateRequest;
import com.zakhrey.vzakhlib.service.SeriesService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/series")
public class SeriesController {

    private final SeriesService seriesService;

    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    /**
     * Создать новую серию
     */
    @PostMapping
    public ResponseEntity<SeriesDto> createSeries(@RequestBody SeriesCreateRequest request) {
        SeriesDto createdSeries = seriesService.createSeries(request);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdSeries.getId())
                .toUri();
        
        return ResponseEntity
                .created(location)
                .body(createdSeries);
    }

    /**
     * Получить список всех серий с пагинацией
     */
    @GetMapping
    public ResponseEntity<PageResponse<SeriesDto>> getAllSeries(
            @PageableDefault(size = 20, sort = "title") Pageable pageable) {
        PageResponse<SeriesDto> seriesPage = seriesService.getAllSeries(pageable);
        return ResponseEntity.ok(seriesPage);
    }

    /**
     * Получить серию по ID
     */
    @GetMapping("/{seriesId}")
    public ResponseEntity<SeriesDto> getSeriesById(@PathVariable UUID seriesId) {
        SeriesDto series = seriesService.getSeriesById(seriesId);
        return ResponseEntity.ok(series);
    }

    /**
     * Обновить серию
     */
    @PutMapping("/{seriesId}")
    public ResponseEntity<SeriesDto> updateSeries(
            @PathVariable UUID seriesId,
            @RequestBody SeriesUpdateRequest request) {
        SeriesDto updatedSeries = seriesService.updateSeries(seriesId, request);
        return ResponseEntity.ok(updatedSeries);
    }

    /**
     * Удалить серию
     */
    @DeleteMapping("/{seriesId}")
    public ResponseEntity<Void> deleteSeries(@PathVariable UUID seriesId) {
        seriesService.deleteSeries(seriesId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получить серию со всеми ее книгами (с пагинацией)
     */
    @GetMapping("/{seriesId}/books")
    public ResponseEntity<PageResponse<BookDto>> getSeriesWithBooks(
            @PathVariable UUID seriesId,
            @PageableDefault(size = 20, sort = "publicationYear") Pageable pageable) {
        PageResponse<BookDto> booksPage = seriesService.getSeriesWithBooks(seriesId, pageable);
        return ResponseEntity.ok(booksPage);
    }

    /**
     * Частичное обновление серии (PATCH)
     */
    @PatchMapping("/{seriesId}")
    public ResponseEntity<SeriesDto> partialUpdateSeries(
            @PathVariable UUID seriesId,
            @RequestBody SeriesUpdateRequest request) {
        // Если ваш сервис не поддерживает PATCH напрямую,
        // можно использовать тот же метод updateSeries
        SeriesDto updatedSeries = seriesService.updateSeries(seriesId, request);
        return ResponseEntity.ok(updatedSeries);
    }
}