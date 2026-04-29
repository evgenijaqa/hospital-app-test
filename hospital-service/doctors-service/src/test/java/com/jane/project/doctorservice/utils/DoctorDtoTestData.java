package com.jane.project.doctorservice.utils;


import com.jane.project.common.dto.doctor.request.DoctorCreateRequest;
import com.jane.project.common.dto.doctor.response.DoctorResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DoctorDtoTestData {

    public static DoctorCreateRequest validRequest() {
        return DoctorCreateRequest.builder()
                .firstName("Іван")
                .lastName("Петренко")
                .contact("0671234567")
                .build();
    }

    public static DoctorResponse validResponse() {
        return DoctorResponse.builder()
                .id(1L)
                .firstName("Іван")
                .lastName("Петренко")
                .contact("0671234567")
                .build();
    }

    public static DoctorCreateRequest invalidPhoneRequest(String phone) {
        return DoctorCreateRequest.builder()
                .firstName("Іван")
                .lastName("Петренко")
                .contact(phone)
                .build();
    }

    public static DoctorCreateRequest blankFieldsRequest() {
        return DoctorCreateRequest.builder()
                .firstName("")
                .lastName("")
                .contact("")
                .build();
    }
}

