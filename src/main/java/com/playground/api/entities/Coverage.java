package com.playground.api.entities;

import com.playground.api.entities.abstracts.DataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "coverages")
public class Coverage extends DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "coverage")
    private Set<PolicyCoverage> policies;

    @OneToMany(mappedBy = "coverage")
    private Set<ClaimCoverage> claims;
}
