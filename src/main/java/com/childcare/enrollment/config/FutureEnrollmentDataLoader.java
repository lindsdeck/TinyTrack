package com.childcare.enrollment.config;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.childcare.enrollment.model.Classroom;
import com.childcare.enrollment.model.Student;
import com.childcare.enrollment.repository.StudentRepository;
import com.childcare.enrollment.service.ClassroomService;
import com.childcare.enrollment.service.StudentService;

@Component
public class FutureEnrollmentDataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final ClassroomService classroomService;

    public FutureEnrollmentDataLoader(
            StudentRepository studentRepository,
            StudentService studentService,
            ClassroomService classroomService) {

        this.studentRepository = studentRepository;
        this.studentService = studentService;
        this.classroomService = classroomService;
    }

    @Override
    public void run(String... args) {

        if (futureDataAlreadyLoaded()) {
            System.out.println(
                    "TinyTrack future enrollment data already exists."
            );
            return;
        }

        addFutureStudents(
                "Infants",
                List.of(
                        seed("Sienna", "Grant",
                                2026, 6, 18,
                                2026, 8, 24),

                        seed("Brooks", "Henderson",
                                2026, 5, 30,
                                2026, 9, 14),

                        seed("Maeve", "Fisher",
                                2026, 7, 2,
                                2026, 10, 5),

                        seed("Silas", "Bryant",
                                2026, 6, 8,
                                2026, 11, 2),

                        seed("June", "Matthews",
                                2026, 7, 19,
                                2026, 12, 7),

                        seed("Beau", "Simmons",
                                2026, 7, 25,
                                2027, 1, 11)
                )
        );

        addFutureStudents(
                "Toddlers",
                List.of(
                        seed("Ada", "Palmer",
                                2025, 8, 20,
                                2026, 9, 8),

                        seed("Rowan", "Mitchell",
                                2025, 9, 12,
                                2026, 10, 19),

                        seed("Wren", "Hayes",
                                2025, 11, 3,
                                2027, 1, 4)
                )
        );

        addFutureStudents(
                "Two's",
                List.of(
                        seed("Emmett", "Dean",
                                2024, 8, 28,
                                2026, 9, 21),

                        seed("Cora", "Spencer",
                                2024, 10, 15,
                                2026, 11, 9),

                        seed("Jasper", "Kennedy",
                                2024, 12, 7,
                                2027, 2, 1)
                )
        );

        addFutureStudents(
                "Three's",
                List.of(
                        seed("Rosie", "Porter",
                                2023, 9, 14,
                                2026, 10, 12),

                        seed("Bennett", "Fleming",
                                2023, 11, 26,
                                2027, 1, 18)
                )
        );

        addFutureStudents(
                "Four's",
                List.of(
                        seed("Lydia", "Harper",
                                2022, 9, 9,
                                2026, 9, 1),

                        seed("Graham", "Stone",
                                2022, 11, 21,
                                2027, 1, 5),

                        seed("Eliza", "Reynolds",
                                2022, 7, 29,
                                2027, 3, 8)
                )
        );

        System.out.println(
                "TinyTrack future enrollment data loaded successfully."
        );
    }

    private boolean futureDataAlreadyLoaded() {

        return studentRepository
                .existsByFirstNameAndLastNameAndDateOfBirth(
                        "Sienna",
                        "Grant",
                        LocalDate.of(2026, 6, 18)
                );
    }

    private void addFutureStudents(
            String classroomName,
            List<FutureStudentSeed> seeds) {

        Classroom classroom =
                classroomService.getActiveClassroomByName(classroomName);

        for (FutureStudentSeed seed : seeds) {

            Student student = new Student(
                    seed.firstName(),
                    seed.lastName(),
                    seed.dateOfBirth(),
                    seed.enrollmentDate(),
                    null,
                    classroom
            );

            studentService.saveStudent(student);
        }
    }

    private FutureStudentSeed seed(
            String firstName,
            String lastName,
            int birthYear,
            int birthMonth,
            int birthDay,
            int enrollmentYear,
            int enrollmentMonth,
            int enrollmentDay) {

        return new FutureStudentSeed(
                firstName,
                lastName,
                LocalDate.of(
                        birthYear,
                        birthMonth,
                        birthDay
                ),
                LocalDate.of(
                        enrollmentYear,
                        enrollmentMonth,
                        enrollmentDay
                )
        );
    }

    private record FutureStudentSeed(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            LocalDate enrollmentDate) {
    }
}