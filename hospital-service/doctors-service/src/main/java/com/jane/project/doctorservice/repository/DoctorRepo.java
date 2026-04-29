package com.jane.project.doctorservice.repository;


import com.jane.project.doctorservice.repository.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepo extends JpaRepository<DoctorEntity, Long> {
    boolean existsByContact(String contact);
}
