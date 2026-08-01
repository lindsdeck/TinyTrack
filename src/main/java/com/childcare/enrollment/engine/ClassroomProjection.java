package com.childcare.enrollment.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.childcare.enrollment.model.Classroom;

public class ClassroomProjection {

    private final Classroom classroom;
    private final List<ProjectedStudent> projectedStudents;

    public ClassroomProjection(Classroom classroom) {
        this.classroom = classroom;
        this.projectedStudents = new ArrayList<>();
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public List<ProjectedStudent> getProjectedStudents() {
        return Collections.unmodifiableList(projectedStudents);
    }

    public void addStudent(ProjectedStudent projectedStudent) {
        projectedStudents.add(projectedStudent);
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