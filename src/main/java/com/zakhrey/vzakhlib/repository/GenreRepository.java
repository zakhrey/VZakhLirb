package com.zakhrey.vzakhlib.repository;

import com.zakhrey.vzakhlib.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenreRepository extends CrudRepository<Genre, UUID> {
    
    // Проверить существование по названию
    boolean existsByName(String name);

    Page<Genre> findAll(Pageable pageable);
}