package com.playground.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Role {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    @OneToMany(mappedBy = "role")
    private Set<User> users;
}
