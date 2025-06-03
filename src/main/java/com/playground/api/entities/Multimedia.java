package com.playground.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private UUID id;

    @NotBlank
    private String uri;

    @NotBlank
    private String mimeType;

    @NotBlank
    private String fileName;

    @OneToMany
    private Set<Product> products;
}
