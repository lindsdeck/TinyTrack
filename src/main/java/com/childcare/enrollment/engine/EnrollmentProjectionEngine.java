package com.childcare.enrollment.engine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.childcare.enrollment.model.Classroom;
import com.childcare.enrollment.model.Student;
import com.childcare.enrollment.service.ClassroomService;
import com.childcare.enrollment.service.StudentService;

@Component
public class EnrollmentProjectionEngine {

    private final StudentService studentService;
    private final ClassroomService classroomService;
    private final AgeCalculator ageCalculator;

    public EnrollmentProjectionEngine(
            StudentService studentService,
            ClassroomService classroomService,
            AgeCalculator ageCalculator) {

        this.studentService = studentService;
        this.classroomService = classroomService;
        this.ageCalculator = ageCalculator;
    }

    public ProjectionResult createProjection(LocalDate projectionDate) {

        if (projectionDate == null) {
            throw new IllegalArgumentException(
                    "Projection date is required."
            );
        }

        ProjectionResult result =
                new ProjectionResult(projectionDate);

        List<Classroom> classrooms =
                getClassroomsOldestToYoungest();

        Map<Long, ClassroomProjection> projectionsByClassroomId =
                createClassroomProjections(classrooms, result);

        List<Student> eligibleStudents =
                getStudentsEligibleOnDate(
                        projectionDate,
                        result
                );

        eligibleStudents.sort(
                Comparator.comparing(Student::getDateOfBirth)
        );

        for (Student student : eligibleStudents) {

            assignStudent(
                    student,
                    projectionDate,
                    classrooms,
                    projectionsByClassroomId,
                    result
            );
        }

        addCapacityWarnings(result);

        return result;
    }

    private List<Classroom> getClassroomsOldestToYoungest() {

        List<Classroom> classrooms =
                new ArrayList<>(
                        classroomService.getActiveClassrooms()
                );

        classrooms.sort(
                Comparator.comparing(
                        Classroom::getMinimumAgeMonths
                ).reversed()
        );

        return classrooms;
    }

    private Map<Long, ClassroomProjection>
            createClassroomProjections(
                    List<Classroom> classrooms,
                    ProjectionResult result) {

        Map<Long, ClassroomProjection> projectionsByClassroomId =
                new HashMap<>();

        for (Classroom classroom : classrooms) {

            ClassroomProjection projection =
                    new ClassroomProjection(classroom);

            result.addClassroomProjection(projection);

            projectionsByClassroomId.put(
                    classroom.getId(),
                    projection
            );
        }

        return projectionsByClassroomId;
    }

    private List<Student> getStudentsEligibleOnDate(
            LocalDate projectionDate,
            ProjectionResult result) {

        List<Student> eligibleStudents = new ArrayList<>();

        for (Student student : studentService.getActiveStudents()) {

            if (student.getEnrollmentDate() != null
                    && student.getEnrollmentDate()
                            .isAfter(projectionDate)) {

                continue;
            }

            if (student.getProjectedExitDate() != null
                    && projectionDate.isAfter(
                            student.getProjectedExitDate())) {

                result.addExitedStudent(student);
                continue;
            }

            if (student.getDateOfBirth() != null
                    && projectionDate.isBefore(
                            student.getDateOfBirth())) {

                result.addWarning(
                        student.getFirstName()
                                + " "
                                + student.getLastName()
                                + " has a birthdate after the "
                                + "selected projection date."
                );

                continue;
            }

            eligibleStudents.add(student);
        }

        return eligibleStudents;
    }

    private void assignStudent(
            Student student,
            LocalDate projectionDate,
            List<Classroom> classrooms,
            Map<Long, ClassroomProjection> projectionsByClassroomId,
            ProjectionResult result) {

        int ageInMonths =
                ageCalculator.calculateAgeInMonths(
                        student.getDateOfBirth(),
                        projectionDate
                );

        Classroom ageEligibleClassroom =
                findAgeEligibleClassroom(
                        ageInMonths,
                        classrooms
                );

        if (ageEligibleClassroom == null) {

            result.addUnplacedStudent(student);

            result.addWarning(
                    student.getFirstName()
                            + " "
                            + student.getLastName()
                            + " does not match any active "
                            + "classroom age range."
            );

            return;
        }

        int preferredClassroomIndex =
                classrooms.indexOf(ageEligibleClassroom);

        Classroom assignedClassroom = null;

        /*
         * Classrooms are ordered oldest to youngest.
         *
         * Begin with the student's age-eligible classroom.
         * If it is full, move toward younger classrooms until
         * an available space is found.
         */
        for (int index = preferredClassroomIndex;
             index < classrooms.size();
             index++) {

            Classroom classroom = classrooms.get(index);

            ClassroomProjection projection =
                    projectionsByClassroomId.get(
                            classroom.getId()
                    );

            if (projection.hasAvailableSpace()) {
                assignedClassroom = classroom;
                break;
            }
        }

        if (assignedClassroom == null) {

            result.addUnplacedStudent(student);

            result.addWarning(
                    student.getFirstName()
                            + " "
                            + student.getLastName()
                            + " could not be placed because "
                            + "all eligible and younger "
                            + "classrooms are full."
            );

            return;
        }

        ProjectionStatus status =
                determineProjectionStatus(
                        student,
                        ageEligibleClassroom,
                        assignedClassroom
                );

        ProjectedStudent projectedStudent =
                new ProjectedStudent(
                        student,
                        ageInMonths,
                        assignedClassroom,
                        ageEligibleClassroom,
                        status
                );

        projectionsByClassroomId
                .get(assignedClassroom.getId())
                .addStudent(projectedStudent);
    }

    private Classroom findAgeEligibleClassroom(
            int ageInMonths,
            List<Classroom> classrooms) {

        if (classrooms.isEmpty()) {
            return null;
        }

        /*
         * The oldest active classroom is special.
         *
         * Children can remain in Four's after turning five
         * until their projected August 25 departure date.
         */
        Classroom oldestClassroom = classrooms.get(0);

        if (ageInMonths
                >= oldestClassroom.getMinimumAgeMonths()) {

            return oldestClassroom;
        }

        for (Classroom classroom : classrooms) {

            boolean meetsMinimumAge =
                    ageInMonths
                            >= classroom.getMinimumAgeMonths();

            boolean belowMaximumAge =
                    ageInMonths
                            < classroom.getMaximumAgeMonths();

            if (meetsMinimumAge && belowMaximumAge) {
                return classroom;
            }
        }

        return null;
    }

    private ProjectionStatus determineProjectionStatus(
            Student student,
            Classroom ageEligibleClassroom,
            Classroom assignedClassroom) {

        if (!assignedClassroom.getId().equals(
                ageEligibleClassroom.getId())) {

            return ProjectionStatus.HELD_FOR_CAPACITY;
        }

        Classroom currentClassroom = student.getClassroom();

        if (currentClassroom != null
                && assignedClassroom.getMinimumAgeMonths()
                    > currentClassroom.getMinimumAgeMonths()) {

            return ProjectionStatus.PROMOTED;
        }

        return ProjectionStatus.NORMAL;
    }

    private void addCapacityWarnings(ProjectionResult result) {

        for (ClassroomProjection projection
                : result.getClassroomProjections()) {

            if (projection.isFull()) {

                result.addWarning(
                        projection.getClassroom()
                                .getClassroomName()
                                + " is projected to be full."
                );
            }
        }

        if (result.getUnplacedStudentCount() > 0) {

            result.addWarning(
                    result.getUnplacedStudentCount()
                            + " student(s) could not be placed "
                            + "within licensed classroom capacity."
            );
        }
    }
}