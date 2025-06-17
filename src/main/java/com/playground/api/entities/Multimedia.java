package com.playground.api.entities;

import com.playground.api.entities.abstracts.DataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "multimedia")
public class Multimedia extends DataEntity {
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
}
