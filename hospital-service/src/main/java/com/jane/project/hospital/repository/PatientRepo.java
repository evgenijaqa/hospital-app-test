package com.jane.project.hospital.repository;

import com.jane.project.hospital.repository.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepo extends JpaRepository<PatientEntity, Long> {
    boolean existsByContact(String contact);
}
