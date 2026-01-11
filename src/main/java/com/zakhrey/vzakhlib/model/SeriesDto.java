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
public class SeriesDto {

    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createTs;
    private LocalDateTime updateTs;
}
