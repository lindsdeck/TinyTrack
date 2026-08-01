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
public class SampleDataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final ClassroomService classroomService;

    public SampleDataLoader(
            StudentRepository studentRepository,
            StudentService studentService,
            ClassroomService classroomService) {

        this.studentRepository = studentRepository;
        this.studentService = studentService;
        this.classroomService = classroomService;
    }

    @Override
    public void run(String... args) {

        if (demoDataAlreadyLoaded()) {
            System.out.println(
                    "TinyTrack sample student data already exists."
            );
            return;
        }

        loadClassroomStudents(
                "Infants",
                8,
                infantStudents()
        );

        loadClassroomStudents(
                "Toddlers",
                9,
                toddlerStudents()
        );

        loadClassroomStudents(
                "Two's",
                10,
                twoYearOldStudents()
        );

        loadClassroomStudents(
                "Three's",
                14,
                threeYearOldStudents()
        );

        loadClassroomStudents(
                "Four's",
                20,
                fourYearOldStudents()
        );

        loadInactiveStudents();

        System.out.println(
                "TinyTrack sample student data loaded successfully."
        );
    }

    private boolean demoDataAlreadyLoaded() {

        return studentRepository
                .existsByFirstNameAndLastNameAndDateOfBirth(
                        "Avery",
                        "Bennett",
                        LocalDate.of(2026, 4, 18)
                );
    }

    private void loadClassroomStudents(
            String classroomName,
            int targetEnrollment,
            List<StudentSeed> studentSeeds) {

        Classroom classroom =
                classroomService.getActiveClassroomByName(classroomName);

        long currentEnrollment =
                studentRepository.countByClassroomAndActiveTrue(classroom);

        int studentsNeeded =
                Math.max(targetEnrollment - (int) currentEnrollment, 0);

        for (int index = 0;
             index < studentsNeeded && index < studentSeeds.size();
             index++) {

            StudentSeed seed = studentSeeds.get(index);

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

    private void loadInactiveStudents() {

        Classroom classroom =
                classroomService.getActiveClassroomByName("Four's");

        List<StudentSeed> inactiveSeeds = List.of(
                new StudentSeed(
                        "Maya",
                        "Lawson",
                        LocalDate.of(2021, 5, 14),
                        LocalDate.of(2024, 8, 19)
                ),
                new StudentSeed(
                        "Eli",
                        "Parker",
                        LocalDate.of(2022, 1, 8),
                        LocalDate.of(2024, 9, 3)
                ),
                new StudentSeed(
                        "Nora",
                        "Walsh",
                        LocalDate.of(2021, 11, 22),
                        LocalDate.of(2025, 1, 6)
                ),
                new StudentSeed(
                        "Caleb",
                        "Martin",
                        LocalDate.of(2022, 3, 30),
                        LocalDate.of(2025, 6, 2)
                )
        );

        for (StudentSeed seed : inactiveSeeds) {

            Student student = new Student(
                    seed.firstName(),
                    seed.lastName(),
                    seed.dateOfBirth(),
                    seed.enrollmentDate(),
                    null,
                    classroom
            );

            Student savedStudent =
                    studentService.saveStudent(student);

            studentService.deactivateStudent(savedStudent.getId());
        }
    }

    private List<StudentSeed> infantStudents() {

        return List.of(
                seed("Avery", "Bennett", 2026, 4, 18, 2026, 6, 1),
                seed("Oliver", "Brooks", 2026, 2, 7, 2026, 5, 18),
                seed("Lucy", "Reed", 2025, 12, 29, 2026, 3, 2),
                seed("Theo", "Collins", 2025, 11, 15, 2026, 2, 9),
                seed("Mia", "Foster", 2025, 10, 3, 2026, 1, 5),
                seed("Jack", "Murphy", 2025, 9, 21, 2025, 12, 1),
                seed("Ella", "Turner", 2025, 8, 14, 2025, 11, 3),
                seed("Leo", "Sullivan", 2025, 8, 2, 2025, 10, 20)
        );
    }

    private List<StudentSeed> toddlerStudents() {

        return List.of(
                seed("Harper", "Coleman", 2025, 7, 11, 2025, 10, 6),
                seed("Henry", "Adams", 2025, 5, 26, 2025, 9, 2),
                seed("Grace", "Miller", 2025, 4, 8, 2025, 8, 18),
                seed("Noah", "Carter", 2025, 2, 19, 2025, 6, 9),
                seed("Hazel", "Morgan", 2024, 12, 30, 2025, 4, 7),
                seed("Miles", "Peterson", 2024, 11, 12, 2025, 3, 3),
                seed("Ruby", "Nelson", 2024, 10, 5, 2025, 1, 6),
                seed("Owen", "Hughes", 2024, 9, 17, 2024, 12, 2),
                seed("Ivy", "Campbell", 2024, 8, 9, 2024, 11, 4)
        );
    }

    private List<StudentSeed> twoYearOldStudents() {

        return List.of(
                seed("Mila", "Johnson", 2024, 7, 18, 2024, 10, 7),
                seed("Liam", "Baker", 2024, 6, 2, 2024, 9, 3),
                seed("Sophie", "Clark", 2024, 4, 24, 2024, 8, 12),
                seed("Finn", "Anderson", 2024, 3, 10, 2024, 7, 1),
                seed("Chloe", "Evans", 2024, 1, 29, 2024, 5, 6),
                seed("Ethan", "Scott", 2023, 12, 17, 2024, 3, 4),
                seed("Sadie", "Wright", 2023, 11, 8, 2024, 2, 5),
                seed("Lucas", "Hill", 2023, 10, 22, 2024, 1, 8),
                seed("Claire", "Green", 2023, 9, 13, 2023, 12, 4),
                seed("Wyatt", "Young", 2023, 8, 6, 2023, 11, 6)
        );
    }

    private List<StudentSeed> threeYearOldStudents() {

        return List.of(
                seed("Emma", "Harris", 2023, 7, 20, 2023, 10, 2),
                seed("Mason", "Walker", 2023, 6, 11, 2023, 9, 5),
                seed("Ava", "Thompson", 2023, 5, 1, 2023, 8, 14),
                seed("Benjamin", "Lewis", 2023, 3, 16, 2023, 7, 10),
                seed("Lily", "Roberts", 2023, 2, 7, 2023, 6, 5),
                seed("James", "Hall", 2022, 12, 28, 2023, 4, 3),
                seed("Isla", "Allen", 2022, 11, 19, 2023, 3, 6),
                seed("Samuel", "King", 2022, 10, 4, 2023, 1, 9),
                seed("Violet", "Bishop", 2022, 9, 15, 2022, 12, 5),
                seed("Daniel", "Ward", 2022, 8, 2, 2022, 11, 7),
                seed("Zoey", "Price", 2022, 7, 12, 2022, 10, 3),
                seed("Logan", "Cooper", 2022, 6, 5, 2022, 9, 6),
                seed("Alice", "Bailey", 2022, 5, 23, 2022, 8, 8),
                seed("Nolan", "Rivera", 2022, 4, 9, 2022, 7, 11)
        );
    }

    private List<StudentSeed> fourYearOldStudents() {

        return List.of(
                seed("Olivia", "Martin", 2022, 7, 24, 2022, 10, 3),
                seed("Theodore", "White", 2022, 6, 13, 2022, 9, 6),
                seed("Charlotte", "Moore", 2022, 5, 2, 2022, 8, 8),
                seed("William", "Taylor", 2022, 3, 18, 2022, 7, 5),
                seed("Amelia", "Jackson", 2022, 2, 9, 2022, 6, 6),
                seed("Elijah", "Thomas", 2021, 12, 27, 2022, 4, 4),
                seed("Evelyn", "Lee", 2021, 11, 16, 2022, 3, 7),
                seed("Hudson", "Brown", 2021, 10, 8, 2022, 1, 10),
                seed("Scarlett", "Davis", 2021, 9, 21, 2021, 12, 6),
                seed("Grayson", "Wilson", 2021, 8, 4, 2021, 11, 8),
                seed("Penelope", "Morris", 2021, 7, 14, 2021, 10, 4),
                seed("Carter", "Rogers", 2021, 6, 3, 2021, 9, 7),
                seed("Layla", "Cook", 2021, 5, 20, 2021, 8, 9),
                seed("Asher", "Bell", 2021, 4, 11, 2021, 7, 6),
                seed("Stella", "Murphy", 2021, 3, 2, 2021, 6, 7),
                seed("Lincoln", "Kelly", 2021, 2, 18, 2021, 5, 3),
                seed("Aurora", "Howard", 2021, 1, 9, 2021, 4, 5),
                seed("Gabriel", "Ross", 2021, 7, 30, 2021, 11, 1),
                seed("Madelyn", "Gray", 2021, 6, 17, 2021, 10, 5),
                seed("Isaac", "Powell", 2021, 5, 6, 2021, 9, 8)
        );
    }

    private StudentSeed seed(
            String firstName,
            String lastName,
            int birthYear,
            int birthMonth,
            int birthDay,
            int enrollmentYear,
            int enrollmentMonth,
            int enrollmentDay) {

        return new StudentSeed(
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

    private record StudentSeed(
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            LocalDate enrollmentDate) {
    }
}