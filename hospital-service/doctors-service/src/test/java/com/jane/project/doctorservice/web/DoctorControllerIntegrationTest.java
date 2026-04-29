package com.jane.project.doctorservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.jane.project.common.dto.doctor.request.DoctorCreateRequest;
import com.jane.project.common.dto.doctor.response.DoctorResponse;
import com.jane.project.common.exceptions.DuplicateContactException;
import com.jane.project.common.exceptions.GlobalExceptionHandler;
import com.jane.project.doctorservice.service.DoctorService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static com.jane.project.doctorservice.utils.DoctorDtoTestData.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DoctorController.class)
class DoctorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class SuccessTests {
        @Test
        void givenValidDoctorData_whenCreateDoctor_thenDoctorIsSuccessfullyRegistered() throws Exception {
            DoctorCreateRequest request = validRequest();
            DoctorResponse response = validResponse();

            Mockito.when(doctorService.createDoctor(any(DoctorCreateRequest.class)))
                    .thenReturn(response);

            mockMvc.perform(post("/api/doctors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(objectMapper.writeValueAsString(response), true));
        }
    }

    @Nested
    class BusinessErrorTests {
        @Test
        void givenDoctorWithContact_whenCreateDoctorWithTheSameContact_thenThrowDuplicateContactError() throws Exception {
            DoctorCreateRequest request = validRequest();

            Mockito.when(doctorService.createDoctor(any(DoctorCreateRequest.class)))
                    .thenThrow(new DuplicateContactException("Лікар з таким контактом вже існує"));

            mockMvc.perform(post("/api/doctors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.type").value("BUSINESS"))
                    .andExpect(jsonPath("$.message").value("Лікар з таким контактом вже існує"))
                    .andExpect(jsonPath("$.extraField").doesNotExist());
        }
    }

    @Nested
    class ValidationErrorTests {
        @Test
        void givenAllFieldsBlank_whenCreateDoctor_thenThrowValidationErrorsForEachField() throws Exception {
            DoctorCreateRequest request = blankFieldsRequest();

            mockMvc.perform(post("/api/doctors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.type").value("VALIDATION"))
                    .andExpect(jsonPath("$.message").value("Некоректні дані запиту"))
                    .andExpect(jsonPath("$.errors[?(@.field=='firstName')].message").value("Ім'я обов'язкове"))
                    .andExpect(jsonPath("$.errors[?(@.field=='lastName')].message").value("Прізвище обов'язкове"))
                    .andExpect(jsonPath("$.errors[?(@.field=='contact')].message").value(containsInAnyOrder("Телефон має складатися з 10 цифр",
                            "Контакт(телефон) обов'язковий")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"12345", "06712abcde", "067-123-4567"})
        void givenInvalidPhoneNumber_whenCreateDoctor_thenThrowPhoneValidationError(String phone) throws Exception {
            DoctorCreateRequest request = invalidPhoneRequest(phone);

            mockMvc.perform(post("/api/doctors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.type").value("VALIDATION"))
                    .andExpect(jsonPath("$.message").value("Некоректні дані запиту"))
                    .andExpect(jsonPath("$.errors[?(@.field=='contact')].message").value("Телефон має складатися з 10 цифр"));
        }

        @Test
        void givenOmitAllFields_whenCreateDoctor_thenThrowValidationErrorsForMissingFields() throws Exception {
            String invalidJson = "{}";

            mockMvc.perform(post("/api/doctors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.type").value("VALIDATION"))
                    .andExpect(jsonPath("$.message").value("Некоректні дані запиту"))
                    .andExpect(jsonPath("$.errors[?(@.field=='firstName')].message").value("Ім'я обов'язкове"))
                    .andExpect(jsonPath("$.errors[?(@.field=='lastName')].message").value("Прізвище обов'язкове"))
                    .andExpect(jsonPath("$.errors[?(@.field=='contact')].message").value("Контакт(телефон) обов'язковий"));
        }
    }

    @Nested
    class SystemErrorTests {
        @Test
        void givenDoctor_whenErrorOnCreateDoctor_thenThrowSystemError() throws Exception {
            DoctorCreateRequest request = validRequest();

            Mockito.when(doctorService.createDoctor(any(DoctorCreateRequest.class)))
                    .thenThrow(new RuntimeException("Unexpected error"));

            mockMvc.perform(post("/api/doctors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.type").value("SYSTEM"))
                    .andExpect(jsonPath("$.message").value("Сталася внутрішня помилка"))
                    .andExpect(jsonPath("$.extraField").doesNotExist());
        }
    }
}
