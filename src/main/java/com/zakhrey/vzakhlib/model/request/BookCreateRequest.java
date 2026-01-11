package com.zakhrey.vzakhlib.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class BookCreateRequest {

    @NotBlank(message = "Название книги обязательно")
    @Size(max = 512, message = "Название книги не должно превышать 512 символов")
    private String name;
    
    @Size(max = 4096, message = "Описание не должно превышать 4096 символов")
    private String description;
    
    private UUID seriesId;
    
    @NotBlank(message = "Язык книги обязателен")
    private String language;
    
    @Size(max = 512, message = "Ссылка на файл не должна превышать 512 символов")
    private String fileLink;
    
    private java.util.Set<UUID> genreIds;
}