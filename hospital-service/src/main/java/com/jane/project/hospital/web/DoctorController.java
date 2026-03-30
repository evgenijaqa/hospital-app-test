package com.jane.project.hospital.web;

import com.jane.project.hospital.dto.ApiErrorResponse;
import com.jane.project.hospital.dto.doctor.request.DoctorCreateRequest;
import com.jane.project.hospital.dto.doctor.response.DoctorResponse;
import com.jane.project.hospital.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    @PostMapping
    @Operation(summary = "Створити лікаря", description = "Додає нового лікаря у систему")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Лікаря успішно створено",
                    content = @Content(schema = @Schema(implementation = DoctorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некоректні дані запиту або лікар з таким контактом вже існує",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody DoctorCreateRequest request) {
        DoctorResponse response = doctorService.createDoctor(request);
        return ResponseEntity.ok(response);
    }
}

