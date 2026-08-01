package com.childcare.enrollment.engine;

import com.childcare.enrollment.model.Classroom;
import com.childcare.enrollment.model.Student;

public class ProjectedStudent {

    private final Student student;
    private final int ageInMonths;
    private final Classroom projectedClassroom;
    private final Classroom ageEligibleClassroom;
    private final ProjectionStatus status;

    public ProjectedStudent(
            Student student,
            int ageInMonths,
            Classroom projectedClassroom,
            Classroom ageEligibleClassroom,
            ProjectionStatus status) {

        this.student = student;
        this.ageInMonths = ageInMonths;
        this.projectedClassroom = projectedClassroom;
        this.ageEligibleClassroom = ageEligibleClassroom;
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public int getAgeInMonths() {
        return ageInMonths;
    }

    public Classroom getProjectedClassroom() {
        return projectedClassroom;
    }

    public Classroom getAgeEligibleClassroom() {
        return ageEligibleClassroom;
    }

    public ProjectionStatus getStatus() {
        return status;
    }

    public int getAgeYears() {
        return ageInMonths / 12;
    }

    public int getRemainingAgeMonths() {
        return ageInMonths % 12;
    }

    public String getDisplayAge() {

        int years = getAgeYears();
        int months = getRemainingAgeMonths();

        if (years == 0) {
            return months + " month" + (months == 1 ? "" : "s");
        }

        if (months == 0) {
            return years + " year" + (years == 1 ? "" : "s");
        }

        return years
                + " year"
                + (years == 1 ? "" : "s")
                + " "
                + months
                + " month"
                + (months == 1 ? "" : "s");
    }
}