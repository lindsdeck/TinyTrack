package com.childcare.enrollment.engine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.childcare.enrollment.model.Student;

public class ProjectionResult {

    private final LocalDate projectionDate;
    private final List<ClassroomProjection> classroomProjections;
    private final List<Student> exitedStudents;
    private final List<Student> unplacedStudents;
    private final List<String> warnings;

    public ProjectionResult(LocalDate projectionDate) {

        this.projectionDate = projectionDate;
        this.classroomProjections = new ArrayList<>();
        this.exitedStudents = new ArrayList<>();
        this.unplacedStudents = new ArrayList<>();
        this.warnings = new ArrayList<>();
    }

    public LocalDate getProjectionDate() {
        return projectionDate;
    }

    public List<ClassroomProjection> getClassroomProjections() {
        return Collections.unmodifiableList(classroomProjections);
    }

    public List<Student> getExitedStudents() {
        return Collections.unmodifiableList(exitedStudents);
    }

    public List<Student> getUnplacedStudents() {
        return Collections.unmodifiableList(unplacedStudents);
    }

    public List<String> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }

    public void addClassroomProjection(
            ClassroomProjection classroomProjection) {

        classroomProjections.add(classroomProjection);
    }

    public void addExitedStudent(Student student) {
        exitedStudents.add(student);
    }

    public void addUnplacedStudent(Student student) {
        unplacedStudents.add(student);
    }

    public void addWarning(String warning) {
        warnings.add(warning);
    }

    public int getTotalProjectedEnrollment() {

        return classroomProjections.stream()
                .mapToInt(ClassroomProjection::getProjectedEnrollment)
                .sum();
    }

    public int getTotalLicensedCapacity() {

        return classroomProjections.stream()
                .mapToInt(ClassroomProjection::getLicensedCapacity)
                .sum();
    }

    public int getTotalProjectedOpenings() {

        return classroomProjections.stream()
                .mapToInt(ClassroomProjection::getProjectedOpenings)
                .sum();
    }

    public int getExitedStudentCount() {
        return exitedStudents.size();
    }

    public int getUnplacedStudentCount() {
        return unplacedStudents.size();
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }
}