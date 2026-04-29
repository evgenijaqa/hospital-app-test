package com.jane.project.patientservice.web;

import com.jane.project.common.dto.ApiErrorResponse;
import com.jane.project.common.dto.patient.request.PatientCreateRequest;
import com.jane.project.common.dto.patient.response.PatientResponse;
import com.jane.project.common.exceptions.GlobalExceptionHandler;
import com.jane.project.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Import(GlobalExceptionHandler.class)
public class PatientController {
    private final PatientService patientService;
    @PostMapping
    @Operation(summary = "Створити пацієнта", description = "Додає нового пацієнта у систему")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пацієнта успішно створено",
                    content = @Content(schema = @Schema(implementation = PatientResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані запиту або пацієнт з таким контактом вже існує",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody PatientCreateRequest request) {
        PatientResponse response = patientService.createPatient(request);
        return ResponseEntity.ok(response);
    }
}
