package com.zakhrey.vzakhlib.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private UUID id;
    private String name;
    private String description;
    private SeriesDto series;
    private String language;
    private String fileLink;
    private LocalDateTime createTs;
    private LocalDateTime updateTs;
    private java.util.Set<GenreDto> genres;
}