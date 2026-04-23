package com.jane.project.hospital.dto.patient.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@Schema(description = "Відповідь про створеного пацієнта")
public class PatientResponse {
    @Schema(description = "Унікальний ID пацієнта")
    private Long id;

    @Schema(description = "Ім'я пацієнта")
    private String firstName;

    @Schema(description = "Прізвище пацієнта")
    private String lastName;

    @Schema(description = "Дата народження пацієнта")
    private LocalDate birthDate;

    @Schema(description = "Контакт пацієнта")
    private String contact;
}
