package com.playground.api.entities;

import com.playground.api.entities.abstracts.DataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "claims")
public class Claim extends DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "number", unique = true, nullable = false)
    private String number;

    @NotNull
    @Column(name = "claim_date", nullable = false)
    private LocalDate claimDate;
}
