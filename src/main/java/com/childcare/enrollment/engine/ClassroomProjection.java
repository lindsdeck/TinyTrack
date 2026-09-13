package com.childcare.enrollment.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.childcare.enrollment.model.Classroom;
import com.childcare.enrollment.model.Student;

public class ClassroomProjection {

    private final Classroom classroom;
    private final List<ProjectedStudent> projectedStudents;
    private final List<Student> upcomingStudents;

    public ClassroomProjection(Classroom classroom) {

        this.classroom = classroom;
        this.projectedStudents = new ArrayList<>();
        this.upcomingStudents = new ArrayList<>();
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public List<ProjectedStudent> getProjectedStudents() {
        return Collections.unmodifiableList(projectedStudents);
    }

    public List<Student> getUpcomingStudents() {
        return Collections.unmodifiableList(upcomingStudents);
    }

    public void addStudent(ProjectedStudent projectedStudent) {
        projectedStudents.add(projectedStudent);
    }

    public void addUpcomingStudent(Student student) {

        upcomingStudents.add(student);

        upcomingStudents.sort(
                Comparator
                        .comparing(Student::getEnrollmentDate)
                        .thenComparing(Student::getDateOfBirth)
        );
    }

    public boolean hasUpcomingStudents() {
        return !upcomingStudents.isEmpty();
    }

    public int getUpcomingStudentCount() {
        return upcomingStudents.size();
    }

    public int getProjectedEnrollment() {
        return projectedStudents.size();
    }

    public int getLicensedCapacity() {
        return classroom.getLicensedCapacity();
    }

    public int getProjectedOpenings() {

        return Math.max(
                getLicensedCapacity() - getProjectedEnrollment(),
                0
        );
    }

    public boolean isFull() {
        return getProjectedEnrollment() >= getLicensedCapacity();
    }

    public boolean hasAvailableSpace() {
        return getProjectedEnrollment() < getLicensedCapacity();
    }
}