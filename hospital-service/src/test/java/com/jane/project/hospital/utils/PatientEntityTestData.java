package com.jane.project.hospital.utils;

import com.jane.project.hospital.repository.entity.PatientEntity;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

import static java.time.LocalDate.now;

@UtilityClass
public class PatientEntityTestData {
    public PatientEntity validPatient(String contact) {
        return PatientEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.parse("1990-05-12"))
                .contact(contact)
                .build();
    }

    public PatientEntity patientWithNullFirstName() {
        return PatientEntity.builder()
                .firstName(null)
                .lastName("Doe")
                .birthDate(LocalDate.parse("1990-05-12"))
                .contact("1234567890")
                .build();
    }

    public PatientEntity patientWithNullLastName() {
        return PatientEntity.builder()
                .firstName("Jane")
                .lastName(null)
                .birthDate(LocalDate.parse("1990-05-12"))
                .contact("1234567890")
                .build();
    }

    public PatientEntity patientWithNullContact() {
        return PatientEntity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .birthDate(LocalDate.parse("1990-05-12"))
                .contact(null)
                .build();
    }

    public PatientEntity patientWithNullDateBirth() {
        return PatientEntity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .birthDate(null)
                .contact("1234567890")
                .build();
    }

    public PatientEntity patientWithEmptyFirstName() {
        return PatientEntity.builder()
                .firstName("")
                .lastName("Doe")
                .birthDate(LocalDate.parse("1990-05-12"))
                .contact("1234567890")
                .build();
    }

    public PatientEntity patientWithEmptyLastName() {
        return PatientEntity.builder()
                .firstName("Jane")
                .lastName("")
                .birthDate(LocalDate.parse("1990-05-12"))
                .contact("1234567890")
                .build();
    }

    public PatientEntity patientWithEmptyContact() {
        return PatientEntity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .birthDate(LocalDate.parse("1990-05-12"))
                .contact("")
                .build();
    }

    public PatientEntity patientWithWhitespaceContact() {
        return PatientEntity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .birthDate(LocalDate.parse("1990-05-12"))
                .contact("          ")
                .build();
    }

    public PatientEntity patientWithBirthDateInPast() {
        return PatientEntity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .birthDate(now().plusDays(1))
                .contact("1234567890")
                .build();
    }
}
