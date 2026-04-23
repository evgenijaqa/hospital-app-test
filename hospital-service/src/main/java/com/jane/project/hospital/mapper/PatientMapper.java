package com.jane.project.hospital.mapper;

import com.jane.project.hospital.dto.patient.request.PatientCreateRequest;
import com.jane.project.hospital.dto.patient.response.PatientResponse;
import com.jane.project.hospital.repository.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(target = "id", ignore = true)
    PatientEntity toEntity(PatientCreateRequest request);
    PatientResponse toResponse(PatientEntity entity);
}
