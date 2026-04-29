package com.jane.project.patientservice.utils;


import com.jane.project.common.dto.patient.request.PatientCreateRequest;
import com.jane.project.common.dto.patient.response.PatientResponse;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

import static java.time.LocalDate.now;

@UtilityClass
public class PatientDtoTestData {
    public static PatientCreateRequest validRequest() {
        return PatientCreateRequest.builder()
                .firstName("Світлана")
                .lastName("Василенко")
                .birthDate(LocalDate.parse("1990-05-12"))
                .contact("0671234567")
                .build();
    }

    public static PatientResponse validResponse() {
        return PatientResponse.builder()
                .id(1L)
                .firstName("Світлана")
                .lastName("Василенко")
                .birthDate(LocalDate.parse("1990-05-12"))
                .contact("0671234567")
                .build();
    }

    public static PatientCreateRequest invalidPhoneRequest(String phone) {
        return PatientCreateRequest.builder()
                .firstName("Світлана")
                .lastName("Василенко")
                .birthDate(LocalDate.parse("1990-05-12"))
                .contact(phone)
                .build();
    }

    public static PatientCreateRequest birthDateInPastRequest() {
        return PatientCreateRequest.builder()
                .firstName("Світлана")
                .lastName("Василенко")
                .birthDate(now().plusDays(1))
                .contact("0671234567")
                .build();
    }

    public static PatientCreateRequest blankFieldsRequest() {
        return PatientCreateRequest.builder()
                .firstName("")
                .lastName("")
                .birthDate(LocalDate.parse("1990-05-12"))
                .contact("")
                .build();
    }
}
