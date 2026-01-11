package com.zakhrey.vzakhlib.mapper;

import com.zakhrey.vzakhlib.entity.Book;
import com.zakhrey.vzakhlib.entity.Genre;
import com.zakhrey.vzakhlib.entity.Language;
import com.zakhrey.vzakhlib.entity.Series;
import com.zakhrey.vzakhlib.model.BookDto;
import com.zakhrey.vzakhlib.model.GenreDto;
import com.zakhrey.vzakhlib.model.SeriesDto;
import lombok.experimental.UtilityClass;

import java.util.stream.Collectors;

@UtilityClass
public class EntityMapper {
    
    public static GenreDto toDto(Genre genre) {
        if (genre == null) return null;
        
        return GenreDto.builder()
            .id(genre.getId())
            .name(genre.getName())
            .description(genre.getDescription())
            .createTs(genre.getCreateTs())
            .updateTs(genre.getUpdateTs())
            .build();
    }
    
    public static Genre toEntity(GenreDto dto) {
        if (dto == null) return null;
        
        return Genre.builder()
            .id(dto.getId())
            .name(dto.getName())
            .description(dto.getDescription())
            .build();
    }
    
    public static SeriesDto toDto(Series series) {
        if (series == null) return null;
        
        return SeriesDto.builder()
            .id(series.getId())
            .name(series.getName())
            .description(series.getDescription())
            .createTs(series.getCreateTs())
            .updateTs(series.getUpdateTs())
            .build();
    }
    
    public static Series toEntity(SeriesDto dto) {
        if (dto == null) return null;
        
        return Series.builder()
            .id(dto.getId())
            .name(dto.getName())
            .description(dto.getDescription())
            .build();
    }
    
    public static BookDto toDto(Book book) {
        if (book == null) return null;
        
        return BookDto.builder()
            .id(book.getId())
            .name(book.getName())
            .description(book.getDescription())
            .series(toDto(book.getSeries()))
            .language(book.getLanguage().name())
            .fileLink(book.getFileLink())
            .createTs(book.getCreateTs())
            .updateTs(book.getUpdateTs())
            .genres(book.getGenres() != null 
                ? book.getGenres().stream()
                    .map(EntityMapper::toDto)
                    .collect(Collectors.toSet())
                : null)
            .build();
    }
    
    public static Book toEntity(BookDto dto) {
        if (dto == null) return null;
        
        return Book.builder()
            .id(dto.getId())
            .name(dto.getName())
            .description(dto.getDescription())
            .language(Language.valueOf(dto.getLanguage()))
            .fileLink(dto.getFileLink())
            .build();
    }
}