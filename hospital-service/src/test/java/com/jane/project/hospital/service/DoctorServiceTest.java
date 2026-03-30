package com.jane.project.hospital.service;

import com.jane.project.hospital.dto.doctor.request.DoctorCreateRequest;
import com.jane.project.hospital.dto.doctor.response.DoctorResponse;
import com.jane.project.hospital.exceptions.DuplicateContactException;
import com.jane.project.hospital.mapper.DoctorMapper;
import com.jane.project.hospital.repository.DoctorRepo;
import com.jane.project.hospital.repository.entity.DoctorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.jane.project.hospital.utils.DoctorDtoTestData.*;
import static com.jane.project.hospital.utils.DoctorEntityTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepo doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    void createDoctor_whenContactIsUnique_returnsDoctorResponse() {
        
        DoctorCreateRequest request = validRequest();
        DoctorEntity entity = validDoctor(request.getContact());
        DoctorEntity savedEntity = entity.toBuilder().id(1L).build();
        DoctorResponse response = validResponse();

        when(doctorRepository.existsByContact(request.getContact())).thenReturn(false);
        when(doctorMapper.toEntity(request)).thenReturn(entity);
        when(doctorRepository.save(entity)).thenReturn(savedEntity);
        when(doctorMapper.toResponse(savedEntity)).thenReturn(response);
        
        DoctorResponse result = doctorService.createDoctor(request);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(response.getId(), result.getId()),
                () -> assertEquals(response.getFirstName(), result.getFirstName()),
                () -> assertEquals(response.getLastName(), result.getLastName()),
                () -> assertEquals(response.getContact(), result.getContact())
        );

        verify(doctorRepository).existsByContact(request.getContact());
        verify(doctorMapper).toEntity(request);
        verify(doctorRepository).save(entity);
        verify(doctorMapper).toResponse(savedEntity);
        verifyNoMoreInteractions(doctorRepository, doctorMapper);
    }

    @Test
    void createDoctor_whenContactExists_throwsDuplicateContactException() {
        
        DoctorCreateRequest request = validRequest();
        when(doctorRepository.existsByContact(request.getContact())).thenReturn(true);

        assertThrows(DuplicateContactException.class, () -> doctorService.createDoctor(request));

        verify(doctorRepository).existsByContact(request.getContact());
        verifyNoInteractions(doctorMapper);
        verify(doctorRepository, never()).save(any());
        verifyNoMoreInteractions(doctorRepository);
    }

    @Test
    void createDoctor_whenSaveFails_throwsRuntimeException() {
        
        DoctorCreateRequest request = validRequest();
        DoctorEntity entity = validDoctor(request.getContact());

        when(doctorRepository.existsByContact(request.getContact())).thenReturn(false);
        when(doctorMapper.toEntity(request)).thenReturn(entity);
        when(doctorRepository.save(entity)).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> doctorService.createDoctor(request));

        assertEquals("DB error", ex.getMessage());

        verify(doctorRepository).existsByContact(request.getContact());
        verify(doctorMapper).toEntity(request);
        verify(doctorRepository).save(entity);
        verifyNoMoreInteractions(doctorMapper, doctorRepository);
    }
}

