package com.jane.project.patientservice.repository;

import com.jane.project.patientservice.repository.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepo extends JpaRepository<PatientEntity, Long> {
    boolean existsByContact(String contact);
}
