package com.playground.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "multimedia", uniqueConstraints = {
        @UniqueConstraint(columnNames = "uri")
})
public class Multimedia {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "uri", unique = true, nullable = false)
    private String uri;

    @NotBlank
    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @NotBlank
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @OneToMany(mappedBy = "multimedia")
    private Set<Product> products;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
