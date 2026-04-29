package com.jane.project.doctorservice.service;


import com.jane.project.common.exceptions.DuplicateContactException;
import com.jane.project.doctorservice.mapper.DoctorMapper;
import com.jane.project.doctorservice.repository.DoctorRepo;
import com.jane.project.common.dto.doctor.request.DoctorCreateRequest;
import com.jane.project.common.dto.doctor.response.DoctorResponse;
import com.jane.project.doctorservice.repository.entity.DoctorEntity;
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

