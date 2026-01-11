package com.zakhrey.vzakhlib.controller.rest;

import com.zakhrey.vzakhlib.model.BookDto;
import com.zakhrey.vzakhlib.model.GenreDto;
import com.zakhrey.vzakhlib.model.request.GenreCreateRequest;
import com.zakhrey.vzakhlib.model.request.GenreUpdateRequest;
import com.zakhrey.vzakhlib.model.PageResponse;
import com.zakhrey.vzakhlib.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreRestController {
    
    private final GenreService genreService;
    
    @PostMapping
    public ResponseEntity<GenreDto> createGenre(@Valid @RequestBody GenreCreateRequest request) {
        GenreDto createdGenre = genreService.createGenre(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGenre);
    }
    
    @GetMapping
    public ResponseEntity<PageResponse<GenreDto>> getAllGenres(
            @PageableDefault(size = 20) Pageable pageable) {
        PageResponse<GenreDto> genres = genreService.getAllGenres(pageable);
        return ResponseEntity.ok(genres);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable UUID id) {
        GenreDto genre = genreService.getGenreById(id);
        return ResponseEntity.ok(genre);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GenreDto> updateGenre(
            @PathVariable UUID id,
            @Valid @RequestBody GenreUpdateRequest request) {
        GenreDto updatedGenre = genreService.updateGenre(id, request);
        return ResponseEntity.ok(updatedGenre);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable UUID id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/books")
    public ResponseEntity<PageResponse<BookDto>> getBooksByGenre(
            @PathVariable UUID id,
            @PageableDefault(size = 20) Pageable pageable) {
        PageResponse<BookDto> books = genreService.getBooksByGenre(id, pageable);
        return ResponseEntity.ok(books);
    }
}