package com.childcare.enrollment.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required.")
    @Size(max = 50, message = "First name cannot exceed 50 characters.")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 50, message = "Last name cannot exceed 50 characters.")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotNull(message = "Date of birth is required.")
    @Past(message = "Date of birth must be in the past.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @NotNull(message = "Enrollment date is required.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "projected_exit_date")
    private LocalDate projectedExitDate;

    @Size(max = 75, message = "Classroom name cannot exceed 75 characters.")
    @Column(name = "classroom_name", length = 75)
    private String classroomName;

    @Column(nullable = false)
    private boolean active = true;

    public Student() {
    }

    public Student(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            LocalDate enrollmentDate,
            LocalDate projectedExitDate,
            String classroomName) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.enrollmentDate = enrollmentDate;
        this.projectedExitDate = projectedExitDate;
        this.classroomName = classroomName;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public LocalDate getProjectedExitDate() {
        return projectedExitDate;
    }

    public void setProjectedExitDate(LocalDate projectedExitDate) {
        this.projectedExitDate = projectedExitDate;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}