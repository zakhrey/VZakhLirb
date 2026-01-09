package com.zakhrey.vzakhlib.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "series")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "books")
public class Series {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "name", length = 512, nullable = false)
    private String name;
    
    @Column(name = "description", length = 4096)
    private String description;
    
    @CreationTimestamp
    @Column(name = "create_ts", updatable = false)
    private LocalDateTime createTs;
    
    @UpdateTimestamp
    @Column(name = "update_ts")
    private LocalDateTime updateTs;

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Book> books;
}