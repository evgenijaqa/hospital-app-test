package com.jane.project.patientservice.service;


import com.jane.project.common.dto.patient.request.PatientCreateRequest;
import com.jane.project.common.dto.patient.response.PatientResponse;
import com.jane.project.common.exceptions.DuplicateContactException;
import com.jane.project.patientservice.mapper.PatientMapper;
import com.jane.project.patientservice.repository.PatientRepo;
import com.jane.project.patientservice.repository.entity.PatientEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.jane.project.patientservice.utils.PatientDtoTestData.*;
import static com.jane.project.patientservice.utils.PatientEntityTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {
    @Mock
    private PatientRepo patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientService patientService;

    @Test
    void createPatient_whenContactIsUnique_returnsPatientResponse() {

        PatientCreateRequest request = validRequest();
        PatientEntity entity = validPatient(request.getContact());
        PatientEntity savedEntity = entity.toBuilder().id(1L).build();
        PatientResponse response = validResponse();

        when(patientRepository.existsByContact(request.getContact())).thenReturn(false);
        when(patientMapper.toEntity(request)).thenReturn(entity);
        when(patientRepository.save(entity)).thenReturn(savedEntity);
        when(patientMapper.toResponse(savedEntity)).thenReturn(response);

        PatientResponse result = patientService.createPatient(request);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(response.getId(), result.getId()),
                () -> assertEquals(response.getFirstName(), result.getFirstName()),
                () -> assertEquals(response.getLastName(), result.getLastName()),
                () -> assertEquals(response.getContact(), result.getContact())
        );

        verify(patientRepository).existsByContact(request.getContact());
        verify(patientMapper).toEntity(request);
        verify(patientRepository).save(entity);
        verify(patientMapper).toResponse(savedEntity);
        verifyNoMoreInteractions(patientRepository, patientMapper);
    }

    @Test
    void createPatient_whenContactExists_throwsDuplicateContactException() {

        PatientCreateRequest request = validRequest();
        when(patientRepository.existsByContact(request.getContact())).thenReturn(true);

        assertThrows(DuplicateContactException.class, () -> patientService.createPatient(request));

        verify(patientRepository).existsByContact(request.getContact());
        verifyNoInteractions(patientMapper);
        verify(patientRepository, never()).save(any());
        verifyNoMoreInteractions(patientRepository);
    }

    @Test
    void createPatient_whenSaveFails_throwsRuntimeException() {

        PatientCreateRequest request = validRequest();
        PatientEntity entity = validPatient(request.getContact());

        when(patientRepository.existsByContact(request.getContact())).thenReturn(false);
        when(patientMapper.toEntity(request)).thenReturn(entity);
        when(patientRepository.save(entity)).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> patientService.createPatient(request));

        assertEquals("DB error", ex.getMessage());

        verify(patientRepository).existsByContact(request.getContact());
        verify(patientMapper).toEntity(request);
        verify(patientRepository).save(entity);
        verifyNoMoreInteractions(patientMapper, patientRepository);
    }
}
