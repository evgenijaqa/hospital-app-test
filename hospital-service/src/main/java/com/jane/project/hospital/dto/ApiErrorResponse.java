package com.jane.project.hospital.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ApiErrorResponse {
    private String type;
    private String message;
    private List<FieldErrorDto> errors;
}

