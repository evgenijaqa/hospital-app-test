package com.jane.project.hospital.service;

import com.jane.project.hospital.dto.doctor.request.DoctorCreateRequest;
import com.jane.project.hospital.dto.doctor.response.DoctorResponse;
import com.jane.project.hospital.exceptions.DuplicateContactException;
import com.jane.project.hospital.mapper.DoctorMapper;
import com.jane.project.hospital.repository.DoctorRepo;
import com.jane.project.hospital.repository.entity.DoctorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepo doctorRepository;
    private final DoctorMapper doctorMapper;

    public DoctorResponse createDoctor(DoctorCreateRequest request) {

        if (doctorRepository.existsByContact(request.getContact())) {
            throw new DuplicateContactException("Лікар з таким контактом вже існує");
        }

        DoctorEntity entity = doctorMapper.toEntity(request);
        DoctorEntity saved = doctorRepository.save(entity);
        return doctorMapper.toResponse(saved);
    }
}

