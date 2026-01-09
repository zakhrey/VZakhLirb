package com.zakhrey.vzakhlib.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"series", "genres"})
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "name", length = 512, nullable = false)
    private String name;
    
    @Column(name = "description", length = 4096)
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Series series;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;
    
    @Column(name = "file_link", length = 512)
    private String fileLink;
    
    @CreationTimestamp
    @Column(name = "create_ts", updatable = false)
    private LocalDateTime createTs;
    
    @UpdateTimestamp
    @Column(name = "update_ts")
    private LocalDateTime updateTs;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "genre_for_book",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;
}