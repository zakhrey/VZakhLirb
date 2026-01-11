package com.zakhrey.vzakhlib.repository;

import com.zakhrey.vzakhlib.entity.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GenreRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GenreRepository genreRepository;

    private Genre genre1;
    private Genre genre2;
    private Genre genre3;

    @BeforeEach
    void setUp() {
        // Очистка базы данных перед каждым тестом
        entityManager.clear();
        
        // Создание тестовых данных
        genre1 = createGenre("Фантастика", "Научная фантастика и фэнтези");
        genre2 = createGenre("Детектив", "Детективные произведения");
        genre3 = createGenre("Роман", "Романические произведения");
        
        // Сохранение через entityManager для контроля над процессом
        entityManager.persist(genre1);
        entityManager.persist(genre2);
        entityManager.persist(genre3);
        entityManager.flush();
    }

    private Genre createGenre(String name, String description) {
        Genre genre = new Genre();
        genre.setName(name);
        genre.setDescription(description);
        return genre;
    }

    @Test
    void whenFindById_thenReturnGenre() {
        // Given
        UUID id = genre1.getId();

        // When
        Optional<Genre> found = genreRepository.findById(id);

        // Then
        assertTrue(found.isPresent());
        assertEquals("Фантастика", found.get().getName());
    }

    @Test
    void whenFindByIdWithInvalidId_thenReturnEmpty() {
        // Given
        UUID invalidId = UUID.randomUUID();

        // When
        Optional<Genre> found = genreRepository.findById(invalidId);

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void whenSaveGenre_thenGenreIsPersisted() {
        // Given
        Genre newGenre = createGenre("Исторический", "Исторические произведения");

        // When
        Genre savedGenre = genreRepository.save(newGenre);
        entityManager.flush();
        entityManager.clear();

        // Then
        Genre foundGenre = entityManager.find(Genre.class, savedGenre.getId());
        assertNotNull(foundGenre);
        assertEquals("Исторический", foundGenre.getName());
        assertEquals("Исторические произведения", foundGenre.getDescription());
    }

    @Test
    void whenUpdateGenre_thenGenreIsUpdated() {
        // Given
        Genre genreToUpdate = entityManager.find(Genre.class, genre1.getId());
        genreToUpdate.setName("Научная фантастика");
        genreToUpdate.setDescription("Обновленное описание");

        // When
        Genre updatedGenre = genreRepository.save(genreToUpdate);
        entityManager.flush();
        entityManager.clear();

        // Then
        Genre foundGenre = entityManager.find(Genre.class, genre1.getId());
        assertEquals("Научная фантастика", foundGenre.getName());
        assertEquals("Обновленное описание", foundGenre.getDescription());
    }

    @Test
    void whenDeleteGenre_thenGenreIsRemoved() {
        // Given
        UUID id = genre1.getId();

        // When
        genreRepository.deleteById(id);
        entityManager.flush();
        entityManager.clear();

        // Then
        Genre foundGenre = entityManager.find(Genre.class, id);
        assertNull(foundGenre);
    }

    @Test
    void whenExistsByName_thenReturnTrue() {
        // When
        boolean exists = genreRepository.existsByName("Фантастика");

        // Then
        assertTrue(exists);
    }

    @Test
    void whenExistsByNameWithNonExistingName_thenReturnFalse() {
        // When
        boolean exists = genreRepository.existsByName("Несуществующий жанр");

        // Then
        assertFalse(exists);
    }

    @Test
    void whenExistsByNameWithCaseSensitive_thenReturnFalse() {
        // When
        boolean exists = genreRepository.existsByName("фантастика"); // lowercase

        // Then
        assertFalse(exists); // Предполагаем, что поиск чувствителен к регистру
    }

    @Test
    void whenFindAllWithPagination_thenReturnPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 2, Sort.by("name").ascending());

        // When
        Page<Genre> page = genreRepository.findAll(pageable);

        // Then
        assertEquals(3, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
        assertEquals(2, page.getContent().size());
        assertEquals("Детектив", page.getContent().get(0).getName());
        assertEquals("Роман", page.getContent().get(1).getName());
    }

    @Test
    void whenFindAllWithSecondPage_thenReturnPage() {
        // Given
        Pageable pageable = PageRequest.of(1, 2, Sort.by("name").ascending());

        // When
        Page<Genre> page = genreRepository.findAll(pageable);

        // Then
        assertEquals(3, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
        assertEquals(1, page.getContent().size());
        assertEquals("Фантастика", page.getContent().get(0).getName());
    }

    @Test
    void whenFindAllWithSortingDesc_thenReturnSortedPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 3, Sort.by("name").descending());

        // When
        Page<Genre> page = genreRepository.findAll(pageable);

        // Then
        assertEquals(3, page.getContent().size());
        assertEquals("Фантастика", page.getContent().get(0).getName());
        assertEquals("Роман", page.getContent().get(1).getName());
        assertEquals("Детектив", page.getContent().get(2).getName());
    }

    @Test
    void whenFindAllWithLargePageSize_thenReturnAllGenres() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Genre> page = genreRepository.findAll(pageable);

        // Then
        assertEquals(3, page.getContent().size());
        assertEquals(3, page.getTotalElements());
        assertEquals(1, page.getTotalPages());
    }

    @Test
    void testCrudOperationsSequence() {
        // Test Create
        Genre newGenre = createGenre("Биография", null);
        Genre savedGenre = genreRepository.save(newGenre);
        assertNotNull(savedGenre.getId());
        
        // Test Read
        Optional<Genre> foundGenre = genreRepository.findById(savedGenre.getId());
        assertTrue(foundGenre.isPresent());
        assertEquals("Биография", foundGenre.get().getName());
        
        // Test Update
        foundGenre.get().setName("Автобиография");
        Genre updatedGenre = genreRepository.save(foundGenre.get());
        assertEquals("Автобиография", updatedGenre.getName());
        
        // Test Delete
        genreRepository.deleteById(updatedGenre.getId());
        Optional<Genre> deletedGenre = genreRepository.findById(updatedGenre.getId());
        assertFalse(deletedGenre.isPresent());
    }

    @Test
    void whenGenreWithSameNameExists_thenExistsByNameReturnsTrue() {
        // When
        boolean exists = genreRepository.existsByName("Фантастика");
        
        // Then
        assertTrue(exists);
    }

    @Test
    void whenSaveMultipleGenres_thenAllArePersisted() {
        // Given
        long initialCount = genreRepository.count();
        Genre genre4 = createGenre("Поэзия", "Стихотворные произведения");
        Genre genre5 = createGenre("Драма", "Драматические произведения");

        // When
        genreRepository.save(genre4);
        genreRepository.save(genre5);
        entityManager.flush();
        entityManager.clear();

        // Then
        long finalCount = genreRepository.count();
        assertEquals(initialCount + 2, finalCount);
    }

    @Test
    void whenFindAllWithEmptyDatabase_thenReturnEmptyPage() {
        // Given
        entityManager.clear();
        // Удаляем все записи
        genreRepository.findAll().forEach(genre -> genreRepository.delete(genre));
        entityManager.flush();
        entityManager.clear();

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Genre> page = genreRepository.findAll(pageable);

        // Then
        assertEquals(0, page.getContent().size());
        assertEquals(0, page.getTotalElements());
        assertEquals(0, page.getTotalPages());
    }
}