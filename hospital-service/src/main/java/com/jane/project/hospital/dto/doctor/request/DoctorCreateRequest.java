package com.jane.project.hospital.dto.doctor.request;

import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@Builder(toBuilder = true)
@Schema(description = "Запит на створення лікаря")
public class DoctorCreateRequest {

    @NotBlank(message = "Ім'я обов'язкове")
    @Schema(description = "Ім'я лікаря", example = "Іван")
    private String firstName;

    @NotBlank(message = "Прізвище обов'язкове")
    @Schema(description = "Прізвище лікаря", example = "Петренко")
    private String lastName;

    @NotBlank(message = "Контакт(телефон) обов'язковий")
    @Pattern(regexp = "\\d{10}", message = "Телефон має складатися з 10 цифр")
    @Schema(description = "Телефон пацієнта (10 цифр)", example = "0671234567")
    private String contact;
}

