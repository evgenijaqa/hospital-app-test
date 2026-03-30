package com.jane.project.hospital.mapper;

import com.jane.project.hospital.dto.doctor.request.DoctorCreateRequest;
import com.jane.project.hospital.dto.doctor.response.DoctorResponse;
import com.jane.project.hospital.repository.entity.DoctorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(target = "id", ignore = true)
    DoctorEntity toEntity(DoctorCreateRequest request);
    DoctorResponse toResponse(DoctorEntity entity);
}

