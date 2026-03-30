package com.jane.project.hospital.repository;

import com.jane.project.hospital.repository.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepo extends JpaRepository<DoctorEntity, Long> {
    boolean existsByContact(String contact);
}
