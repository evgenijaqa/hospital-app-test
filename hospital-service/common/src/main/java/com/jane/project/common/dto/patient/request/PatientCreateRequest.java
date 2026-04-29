package com.jane.project.common.dto.patient.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@Schema(description = "Запит на створення пацієнта")
public class PatientCreateRequest {
    @NotBlank(message = "Ім'я обов'язкове")
    @Schema(description = "Ім'я пацієнта", example = "Світлана")
    private String firstName;

    @NotBlank(message = "Прізвище обов'язкове")
    @Schema(description = "Прізвище пацієнта", example = "Василенко")
    private String lastName;

    @NotNull(message = "Дата народження обов'язкова")
    @Past(message = "Дата народження має бути в минулому")
    @Schema(description = "Дата народження пацієнта", example = "1990-05-12")
    private LocalDate birthDate;

    @NotBlank(message = "Контакт(телефон) обов'язковий")
    @Pattern(regexp = "\\d{10}", message = "Телефон має складатися з 10 цифр")
    @Schema(description = "Телефон пацієнта (10 цифр)", example = "0671234567")
    private String contact;
}
