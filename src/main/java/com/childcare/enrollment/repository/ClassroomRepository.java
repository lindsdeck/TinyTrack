package com.childcare.enrollment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.childcare.enrollment.model.Classroom;

public interface ClassroomRepository
        extends JpaRepository<Classroom, Long> {

    List<Classroom> findByActiveTrueOrderByClassroomNameAsc();

    long countByActiveTrue();
}