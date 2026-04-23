package com.jane.project.hospital.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jane.project.hospital.dto.patient.request.PatientCreateRequest;
import com.jane.project.hospital.dto.patient.response.PatientResponse;
import com.jane.project.hospital.exceptions.DuplicateContactException;
import com.jane.project.hospital.service.PatientService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.jane.project.hospital.utils.PatientDtoTestData.*;
import static java.time.LocalDate.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PatientController.class)
public class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class SuccessTests {
        @Test
        void givenValidPatientData_whenCreatePatient_thenPatientIsSuccessfullyRegistered() throws Exception {
            PatientCreateRequest request = validRequest();
            PatientResponse response = validResponse();

            Mockito.when(patientService.createPatient(any(PatientCreateRequest.class)))
                    .thenReturn(response);

            mockMvc.perform(post("/api/patients")
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
        void givenPatientWithContact_whenCreatePatientWithTheSameContact_thenThrowDuplicateContactError() throws Exception {
            PatientCreateRequest request = validRequest();

            when(patientService.createPatient(any(PatientCreateRequest.class)))
                    .thenThrow(new DuplicateContactException("Пацієнт з таким контактом вже існує"));

            mockMvc.perform(post("/api/patients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.type").value("BUSINESS"))
                    .andExpect(jsonPath("$.message").value("Пацієнт з таким контактом вже існує"))
                    .andExpect(jsonPath("$.extraField").doesNotExist());
        }
    }

    @Nested
    class ValidationErrorTests {
        @Test
        void givenAllFieldsBlank_whenCreatePatient_thenThrowValidationErrorsForEachField() throws Exception {
            PatientCreateRequest request = blankFieldsRequest();

            mockMvc.perform(post("/api/patients")
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
        void givenInvalidPhoneNumber_whenCreatePatient_thenThrowPhoneValidationError(String phone) throws Exception {
            PatientCreateRequest request = invalidPhoneRequest(phone);

            mockMvc.perform(post("/api/patients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.type").value("VALIDATION"))
                    .andExpect(jsonPath("$.message").value("Некоректні дані запиту"))
                    .andExpect(jsonPath("$.errors[?(@.field=='contact')].message").value("Телефон має складатися з 10 цифр"));
        }

        @Test
        void givenBirthDateInPast_whenCreatePatient_thenThrowBirthDateValidationError() throws Exception {
            PatientCreateRequest request = birthDateInPastRequest();

            mockMvc.perform(post("/api/patients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.type").value("VALIDATION"))
                    .andExpect(jsonPath("$.message").value("Некоректні дані запиту"))
                    .andExpect(jsonPath("$.errors[?(@.field=='birthDate')].message").value("Дата народження має бути в минулому"));
        }

        @Test
        void givenOmitAllFields_whenCreatePatient_thenThrowValidationErrorsForMissingFields() throws Exception {
            String invalidJson = "{}";

            mockMvc.perform(post("/api/patients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.type").value("VALIDATION"))
                    .andExpect(jsonPath("$.message").value("Некоректні дані запиту"))
                    .andExpect(jsonPath("$.errors[?(@.field=='firstName')].message").value("Ім'я обов'язкове"))
                    .andExpect(jsonPath("$.errors[?(@.field=='lastName')].message").value("Прізвище обов'язкове"))
                    .andExpect(jsonPath("$.errors[?(@.field=='birthDate')].message").value("Дата народження обов'язкова"))
                    .andExpect(jsonPath("$.errors[?(@.field=='contact')].message").value("Контакт(телефон) обов'язковий"));
        }
    }

    @Nested
    class SystemErrorTests {
        @Test
        void givenDoctor_whenErrorOnCreatePatient_thenThrowSystemError() throws Exception {
            PatientCreateRequest request = validRequest();

            when(patientService.createPatient(any(PatientCreateRequest.class)))
                    .thenThrow(new RuntimeException("Unexpected error"));

            mockMvc.perform(post("/api/patients")
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
