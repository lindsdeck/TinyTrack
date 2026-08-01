package com.childcare.enrollment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.childcare.enrollment.model.Center;

public interface CenterRepository extends JpaRepository<Center, Long> {

    List<Center> findByActiveTrue();
}