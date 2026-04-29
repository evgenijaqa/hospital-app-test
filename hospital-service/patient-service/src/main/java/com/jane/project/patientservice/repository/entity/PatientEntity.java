package com.jane.project.patientservice.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "patients", uniqueConstraints = {
        @UniqueConstraint(columnNames = "contact")
})
@Data
@Builder(toBuilder = true)
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    @NotNull
    private String firstName;

    @Column
    @NotBlank
    @NotNull
    private String lastName;

    @Column
    @NotNull
    @Past
    private LocalDate birthDate;

    @Column
    @NotBlank
    @NotNull
    @Size(min = 10, max = 10, message = "Contact must be exactly 10 digits")
    private String contact;
}
