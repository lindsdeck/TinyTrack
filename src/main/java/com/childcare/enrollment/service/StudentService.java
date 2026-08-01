package com.childcare.enrollment.service;

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
        return studentRepository.save(student);
    }

    public long countActiveStudents() {
        return studentRepository.countByActiveTrue();
    }

    public Student updateStudent(Long id, Student updatedStudent) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student not found with ID: " + id));

        existingStudent.setFirstName(updatedStudent.getFirstName());
        existingStudent.setLastName(updatedStudent.getLastName());
        existingStudent.setDateOfBirth(updatedStudent.getDateOfBirth());
        existingStudent.setEnrollmentDate(updatedStudent.getEnrollmentDate());
        existingStudent.setProjectedExitDate(
                updatedStudent.getProjectedExitDate());
        existingStudent.setClassroomName(
                updatedStudent.getClassroomName());

        return studentRepository.save(existingStudent);
    }

    public void deactivateStudent(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student not found with ID: " + id));

        student.setActive(false);
        studentRepository.save(student);
    }

    public void reactivateStudent(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student not found with ID: " + id));

        student.setActive(true);
        studentRepository.save(student);
    }
}