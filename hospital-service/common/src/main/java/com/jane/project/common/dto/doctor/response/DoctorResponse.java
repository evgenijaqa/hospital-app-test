package com.jane.project.common.dto.doctor.response;

import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder(toBuilder = true)
@Schema(description = "Відповідь про створеного лікаря")
public class DoctorResponse {

    @Schema(description = "Унікальний ID лікаря")
    private Long id;

    @Schema(description = "Ім'я лікаря")
    private String firstName;

    @Schema(description = "Прізвище лікаря")
    private String lastName;

    @Schema(description = "Контакт лікаря")
    private String contact;
}

