package com.childcare.enrollment.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.childcare.enrollment.model.Student;
import com.childcare.enrollment.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> getActiveStudents() {
        return studentRepository
                .findByActiveTrueOrderByLastNameAscFirstNameAsc();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student saveStudent(Student student) {

        student.setProjectedExitDate(
                calculateProjectedExitDate(student.getDateOfBirth())
        );

        return studentRepository.save(student);
    }

    public long countActiveStudents() {
        return studentRepository.countByActiveTrue();
    }

    public Student updateStudent(Long id, Student updatedStudent) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student not found with ID: " + id
                ));

        existingStudent.setFirstName(updatedStudent.getFirstName());
        existingStudent.setLastName(updatedStudent.getLastName());
        existingStudent.setDateOfBirth(updatedStudent.getDateOfBirth());
        existingStudent.setEnrollmentDate(updatedStudent.getEnrollmentDate());
        existingStudent.setClassroom(updatedStudent.getClassroom());

        existingStudent.setProjectedExitDate(
                calculateProjectedExitDate(
                        updatedStudent.getDateOfBirth()
                )
        );

        return studentRepository.save(existingStudent);
    }

    public void deactivateStudent(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student not found with ID: " + id
                ));

        student.setActive(false);
        studentRepository.save(student);
    }

    public void reactivateStudent(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student not found with ID: " + id
                ));

        student.setActive(true);
        studentRepository.save(student);
    }

    public LocalDate calculateProjectedExitDate(LocalDate dateOfBirth) {

        if (dateOfBirth == null) {
            return null;
        }

        int yearStudentTurnsFive =
                dateOfBirth.plusYears(5).getYear();

        return LocalDate.of(
                yearStudentTurnsFive,
                Month.AUGUST,
                25
        );
    }

    public List<Student> searchActiveStudents(String searchTerm) {

    if (searchTerm == null || searchTerm.isBlank()) {
        return getActiveStudents();
    }

    String cleanedSearchTerm = searchTerm.trim();

    return studentRepository
            .findByActiveTrueAndFirstNameContainingIgnoreCaseOrActiveTrueAndLastNameContainingIgnoreCaseOrderByLastNameAscFirstNameAsc(
                    cleanedSearchTerm,
                    cleanedSearchTerm
            );
}

public long countCurrentlyEnrolledStudents() {

    LocalDate today = LocalDate.now();

    return studentRepository
            .countByActiveTrueAndEnrollmentDateLessThanEqualAndProjectedExitDateGreaterThanEqual(
                    today,
                    today
            );
}
}