package com.jane.project.patientservice.service;


import com.jane.project.common.dto.patient.request.PatientCreateRequest;
import com.jane.project.common.dto.patient.response.PatientResponse;
import com.jane.project.common.exceptions.DuplicateContactException;
import com.jane.project.patientservice.mapper.PatientMapper;
import com.jane.project.patientservice.repository.PatientRepo;
import com.jane.project.patientservice.repository.entity.PatientEntity;
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
