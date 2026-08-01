package com.childcare.enrollment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.childcare.enrollment.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByActiveTrueOrderByLastNameAscFirstNameAsc();

    long countByActiveTrue();
}