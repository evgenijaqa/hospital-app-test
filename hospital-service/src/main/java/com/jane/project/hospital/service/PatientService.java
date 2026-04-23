package com.jane.project.hospital.service;

import com.jane.project.hospital.dto.patient.request.PatientCreateRequest;
import com.jane.project.hospital.dto.patient.response.PatientResponse;
import com.jane.project.hospital.exceptions.DuplicateContactException;
import com.jane.project.hospital.mapper.PatientMapper;
import com.jane.project.hospital.repository.PatientRepo;
import com.jane.project.hospital.repository.entity.PatientEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepo patientRepo;
    private final PatientMapper patientMapper;

    public PatientResponse createPatient(PatientCreateRequest request) {

        if (patientRepo.existsByContact(request.getContact())) {
            throw new DuplicateContactException("Пацієнт з таким контактом вже існує");
        }

        PatientEntity entity = patientMapper.toEntity(request);
        PatientEntity saved = patientRepo.save(entity);
        return patientMapper.toResponse(saved);
    }
}
