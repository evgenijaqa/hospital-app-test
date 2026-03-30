package com.jane.project.hospital.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.jane.project.hospital.repository.entity.DoctorEntity;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.stream.Stream;

import static com.jane.project.hospital.utils.DoctorEntityTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DBRider
class DoctorRepoTest {

    @Autowired
    private DoctorRepo doctorRepo;

    @Nested
    class ExistsByContactTests {
        @Test
        @DataSet("doctors.xml")
        void givenSavedDoctors_whenCheckingNotExistingContact_thenReturnFalse() {
            assertThat(doctorRepo.existsByContact("1234567890")).isFalse();
        }

        @Test
        @DataSet("doctors.xml")
        void givenSavedDoctors_whenCheckingExistingContact_thenReturnTrue() {
            assertThat(doctorRepo.existsByContact("1111111111")).isTrue();
        }
    }

    @Nested
    class SaveDoctorTests {

        @Test
        @DataSet("doctors.xml")
        void givenValidDoctor_whenSaving_thenPersistSuccessfully() {
            DoctorEntity saved = doctorRepo.save(validDoctor("2222222222"));
            assertThat(saved.getId()).isNotNull();
            assertThat(saved)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(validDoctor("2222222222"));
        }

        @Test
        @DataSet("doctors.xml")
        void givenDuplicateContact_whenSaving_thenThrowDataIntegrityViolation() {
            assertThatThrownBy(() -> {
                doctorRepo.save(validDoctor("1111111111"));
            })
                    .isInstanceOf(DataIntegrityViolationException.class);
        }

        @ParameterizedTest
        @CsvSource({
                "123456789",     // 9 символів
                "12345678901"    // 11 символів
        })
        @DataSet("doctors.xml")
        void givenInvalidContactLength_whenSaving_thenThrowConstraintViolation(String invalidContact) {
             assertThatThrownBy(() -> {
                doctorRepo.save(validDoctor(invalidContact));
            })
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContaining("Contact must be exactly 10 digits");
        }

        static Stream<DoctorEntity> invalidDoctors() {
            return Stream.of(
                    doctorWithNullFirstName(),
                    doctorWithNullLastName(),
                    doctorWithNullContact(),
                    doctorWithEmptyFirstName(),
                    doctorWithEmptyLastName(),
                    doctorWithEmptyContact()
            );
        }

        @ParameterizedTest
        @MethodSource("invalidDoctors")
        @DataSet("doctors.xml")
        void givenNullMandatoryField_whenSaving_thenThrowDataIntegrityViolation(DoctorEntity invalid) {
            assertThatThrownBy(() -> {
                doctorRepo.save(invalid);
            }).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DataSet("doctors.xml")
        void givenWhitespaceContact_whenSaving_thenThrowConstraintViolation() {
            assertThatThrownBy(() -> doctorRepo.save(validDoctor("          ")))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }

}

