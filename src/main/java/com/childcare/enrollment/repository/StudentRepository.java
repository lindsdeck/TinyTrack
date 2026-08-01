package com.childcare.enrollment.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.childcare.enrollment.model.Classroom;
import com.childcare.enrollment.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByActiveTrueOrderByLastNameAscFirstNameAsc();

    List<Student>
            findByActiveTrueAndFirstNameContainingIgnoreCaseOrActiveTrueAndLastNameContainingIgnoreCaseOrderByLastNameAscFirstNameAsc(
                    String firstName,
                    String lastName
            );

    long countByActiveTrue();

    long countByClassroomAndActiveTrue(Classroom classroom);

    boolean existsByFirstNameAndLastNameAndDateOfBirth(
            String firstName,
            String lastName,
            LocalDate dateOfBirth
    );

    long countByActiveTrueAndEnrollmentDateLessThanEqualAndProjectedExitDateGreaterThanEqual(
        LocalDate enrollmentDate,
        LocalDate projectedExitDate
);
}