package com.jane.project.hospital.utils;

import com.jane.project.hospital.repository.entity.DoctorEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DoctorEntityTestData {

    public DoctorEntity validDoctor(String contact) {
        return DoctorEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .contact(contact)
                .build();
    }

    public DoctorEntity doctorWithNullFirstName() {
        return DoctorEntity.builder()
                .firstName(null)
                .lastName("Doe")
                .contact("1234567890")
                .build();
    }

    public DoctorEntity doctorWithNullLastName() {
        return DoctorEntity.builder()
                .firstName("Jane")
                .lastName(null)
                .contact("1234567890")
                .build();
    }

    public DoctorEntity doctorWithNullContact() {
        return DoctorEntity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .contact(null)
                .build();
    }

    public DoctorEntity doctorWithEmptyFirstName() {
        return DoctorEntity.builder()
                .firstName("")
                .lastName("Doe")
                .contact("1234567890")
                .build();
    }

    public DoctorEntity doctorWithEmptyLastName() {
        return DoctorEntity.builder()
                .firstName("Jane")
                .lastName("")
                .contact("1234567890")
                .build();
    }

    public DoctorEntity doctorWithEmptyContact() {
        return DoctorEntity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .contact("")
                .build();
    }
}

