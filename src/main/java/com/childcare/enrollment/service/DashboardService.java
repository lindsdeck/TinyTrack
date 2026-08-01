package com.childcare.enrollment.service;

import org.springframework.stereotype.Service;

import com.childcare.enrollment.model.DashboardSummary;

@Service
public class DashboardService {

    private final StudentService studentService;
    private final ClassroomService classroomService;

    public DashboardService(
            StudentService studentService,
            ClassroomService classroomService) {

        this.studentService = studentService;
        this.classroomService = classroomService;
    }

    public DashboardSummary getDashboardSummary() {

        long activeStudentCount =
                studentService.countCurrentlyEnrolledStudents();

        long activeClassroomCount =
                classroomService.countActiveClassrooms();

        int totalLicensedCapacity =
                classroomService.getTotalActiveLicensedCapacity();

        long currentOpenings = Math.max(
                totalLicensedCapacity - activeStudentCount,
                0
        );

        return new DashboardSummary(
                activeStudentCount,
                activeClassroomCount,
                totalLicensedCapacity,
                currentOpenings
        );
    }
}