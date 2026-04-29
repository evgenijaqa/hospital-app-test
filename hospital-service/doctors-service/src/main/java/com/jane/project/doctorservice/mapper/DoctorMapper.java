package com.jane.project.doctorservice.mapper;

import com.jane.project.doctorservice.repository.entity.DoctorEntity;
import com.jane.project.common.dto.doctor.request.DoctorCreateRequest;
import com.jane.project.common.dto.doctor.response.DoctorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(target = "id", ignore = true)
    DoctorEntity toEntity(DoctorCreateRequest request);
    DoctorResponse toResponse(DoctorEntity entity);
}

