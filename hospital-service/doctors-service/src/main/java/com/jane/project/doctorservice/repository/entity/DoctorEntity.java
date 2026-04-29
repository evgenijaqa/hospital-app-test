package com.jane.project.doctorservice.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "doctors", uniqueConstraints = {
        @UniqueConstraint(columnNames = "contact")
})
@Data
@Builder(toBuilder = true)
public class DoctorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    @NotNull
    private String firstName;

    @Column(nullable = false)
    @NotBlank
    @NotNull
    private String lastName;

    @Column(nullable = false, unique = true, length = 10)
    @Size(min = 10, max = 10, message = "Contact must be exactly 10 digits")
    @NotBlank
    private String contact;
}
