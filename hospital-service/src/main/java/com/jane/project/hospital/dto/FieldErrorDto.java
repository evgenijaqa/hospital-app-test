package com.jane.project.hospital.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldErrorDto {
    private String field;
    private String message;
}
