package com.childcare.enrollment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.childcare.enrollment.model.Classroom;
import com.childcare.enrollment.repository.ClassroomRepository;

@Service
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    public List<Classroom> getActiveClassrooms() {
        return classroomRepository
                .findByActiveTrueOrderByClassroomNameAsc();
    }

    public Optional<Classroom> getClassroomById(Long id) {
        return classroomRepository.findById(id);
    }

    public Classroom saveClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    public long countActiveClassrooms() {
        return classroomRepository.countByActiveTrue();
    }

    public Classroom updateClassroom(
            Long id,
            Classroom updatedClassroom) {

        Classroom existingClassroom =
                classroomRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Classroom not found with ID: " + id
                                ));

        existingClassroom.setClassroomName(
                updatedClassroom.getClassroomName());

        existingClassroom.setMinimumAgeMonths(
                updatedClassroom.getMinimumAgeMonths());

        existingClassroom.setMaximumAgeMonths(
                updatedClassroom.getMaximumAgeMonths());

        existingClassroom.setLicensedCapacity(
                updatedClassroom.getLicensedCapacity());

        existingClassroom.setCenter(
                updatedClassroom.getCenter());

        return classroomRepository.save(existingClassroom);
    }

    public void deactivateClassroom(Long id) {

        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Classroom not found with ID: " + id
                ));

        classroom.setActive(false);
        classroomRepository.save(classroom);
    }

    public void reactivateClassroom(Long id) {

        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Classroom not found with ID: " + id
                ));

        classroom.setActive(true);
        classroomRepository.save(classroom);
    }
    public int getTotalActiveLicensedCapacity() {

    return classroomRepository
            .findByActiveTrueOrderByClassroomNameAsc()
            .stream()
            .mapToInt(Classroom::getLicensedCapacity)
            .sum();
}

public Classroom getActiveClassroomByName(String classroomName) {

    return classroomRepository
            .findByClassroomNameIgnoreCaseAndActiveTrue(classroomName)
            .orElseThrow(() -> new IllegalArgumentException(
                    "Active classroom not found: " + classroomName
            ));
}
}