package com.playground.api.entities;

import com.playground.api.entities.abstracts.DataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Table(name = "roles")
public class Role extends DataEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "role")
    private Set<User> users;
}
