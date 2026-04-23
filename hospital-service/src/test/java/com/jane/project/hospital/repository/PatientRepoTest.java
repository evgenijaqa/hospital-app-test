package com.jane.project.hospital.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.jane.project.hospital.repository.entity.PatientEntity;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.stream.Stream;

import static com.jane.project.hospital.utils.PatientEntityTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DBRider
public class PatientRepoTest {
    @Autowired
    private PatientRepo patientRepo;

    @Nested
    class ExistsByContactTests {
        @Test
        @DataSet("patients.xml")
        void givenSavedPatient_whenCheckingNotExistingContact_thenReturnFalse() {
            assertThat(patientRepo.existsByContact("1234567890")).isFalse();
        }

        @Test
        @DataSet("patients.xml")
        void givenSavedPatient_whenCheckingExistingContact_thenReturnTrue() {
            assertThat(patientRepo.existsByContact("1111111111")).isTrue();
        }
    }

    @Nested
    class SavePatientTests {

        @Test
        void givenValidPatient_whenSaving_thenPersistSuccessfully() {
            PatientEntity saved = patientRepo.save(validPatient("2222222222"));
            assertThat(saved.getId()).isNotNull();
            assertThat(saved)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(validPatient("2222222222"));
        }

        @Test
        @DataSet("patients.xml")
        void givenDuplicateContact_whenSaving_thenThrowDataIntegrityViolation() {
            assertThatThrownBy(() -> {
                patientRepo.save(validPatient("1111111111"));
            })
                    .isInstanceOf(DataIntegrityViolationException.class);
        }

        @ParameterizedTest
        @CsvSource({
                "123456789",     // 9 символів
                "12345678901"    // 11 символів
        })
        void givenInvalidContactLength_whenSaving_thenThrowConstraintViolation(String invalidContact) {
            assertThatThrownBy(() -> {
                patientRepo.save(validPatient(invalidContact));
            })
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("Contact must be exactly 10 digits");
        }

        @Test
        void givenWhitespaceContact_whenSaving_thenThrowDataConstraintViolation() {
            assertThatThrownBy(() -> {
                patientRepo.save(patientWithWhitespaceContact());
            })
                    .isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        void givenBirthDateInPast_whenSaving_thenThrowDataConstraintViolation() {
            assertThatThrownBy(() -> {
                patientRepo.save(patientWithBirthDateInPast());
            })
                    .isInstanceOf(ConstraintViolationException.class);
        }

        static Stream<PatientEntity> patientsWithNullMandatoryField() {
            return Stream.of(
                    patientWithNullFirstName(),
                    patientWithNullLastName(),
                    patientWithNullDateBirth(),
                    patientWithNullContact(),
                    patientWithWhitespaceContact(),
                    patientWithBirthDateInPast()
            );
        }

        @ParameterizedTest
        @MethodSource("patientsWithNullMandatoryField")
        void givenNullMandatoryField_whenSaving_thenThrowDataConstraintViolation(PatientEntity invalid) {
            assertThatThrownBy(() -> {
                patientRepo.save(invalid);
            }).isInstanceOf(ConstraintViolationException.class);
        }

        static Stream<PatientEntity> patientsWithEmptyMandatoryField() {
            return Stream.of(
                    patientWithEmptyFirstName(),
                    patientWithEmptyLastName(),
                    patientWithEmptyContact()
            );
        }

        @ParameterizedTest
        @MethodSource("patientsWithEmptyMandatoryField")
        void givenEmptyMandatoryField_whenSaving_thenThrowDataConstraintViolation(PatientEntity invalid) {
            assertThatThrownBy(() -> {
                patientRepo.save(invalid);
            }).isInstanceOf(ConstraintViolationException.class);
        }

    }
}
