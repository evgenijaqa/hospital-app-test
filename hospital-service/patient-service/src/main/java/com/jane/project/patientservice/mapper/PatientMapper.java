package com.jane.project.patientservice.mapper;

import com.jane.project.common.dto.patient.request.PatientCreateRequest;
import com.jane.project.common.dto.patient.response.PatientResponse;
import com.jane.project.patientservice.repository.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(target = "id", ignore = true)
    PatientEntity toEntity(PatientCreateRequest request);
    PatientResponse toResponse(PatientEntity entity);
}
