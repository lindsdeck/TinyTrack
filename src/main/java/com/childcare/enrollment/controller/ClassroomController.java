package com.childcare.enrollment.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.childcare.enrollment.model.Center;
import com.childcare.enrollment.model.Classroom;
import com.childcare.enrollment.service.CenterService;
import com.childcare.enrollment.service.ClassroomService;

import jakarta.validation.Valid;

@Controller
public class ClassroomController {

    private final ClassroomService classroomService;
    private final CenterService centerService;

    public ClassroomController(
            ClassroomService classroomService,
            CenterService centerService) {

        this.classroomService = classroomService;
        this.centerService = centerService;
    }

    @GetMapping("/classrooms")
    public String viewClassrooms(Model model) {

        model.addAttribute(
                "classrooms",
                classroomService.getActiveClassrooms()
        );

        model.addAttribute("activePage", "classrooms");

        return "classrooms";
    }

    @GetMapping("/classrooms/new")
    public String showAddClassroomForm(Model model) {

        Classroom classroom = new Classroom();

        List<Center> activeCenters = centerService.getActiveCenters();

        if (activeCenters.size() == 1) {
            classroom.setCenter(activeCenters.get(0));
        }

        model.addAttribute("classroom", classroom);
        model.addAttribute("centers", activeCenters);
        model.addAttribute("activePage", "classrooms");

        return "classroom-form";
    }

    @PostMapping("/classrooms/save")
    public String saveClassroom(
            @Valid @ModelAttribute("classroom") Classroom classroom,
            BindingResult bindingResult,
            Model model) {

        validateAgeRange(classroom, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute(
                    "centers",
                    centerService.getActiveCenters()
            );

            model.addAttribute("activePage", "classrooms");

            return "classroom-form";
        }

        classroomService.saveClassroom(classroom);

        return "redirect:/classrooms";
    }

    @GetMapping("/classrooms/{id}/edit")
    public String showEditClassroomForm(
            @PathVariable Long id,
            Model model) {

        Classroom classroom = classroomService
                .getClassroomById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Classroom not found with ID: " + id
                ));

        model.addAttribute("classroom", classroom);
        model.addAttribute(
                "centers",
                centerService.getActiveCenters()
        );

        model.addAttribute("activePage", "classrooms");

        return "classroom-form";
    }

    @PostMapping("/classrooms/{id}/update")
    public String updateClassroom(
            @PathVariable Long id,
            @Valid @ModelAttribute("classroom") Classroom classroom,
            BindingResult bindingResult,
            Model model) {

        validateAgeRange(classroom, bindingResult);

        if (bindingResult.hasErrors()) {
            classroom.setId(id);

            model.addAttribute(
                    "centers",
                    centerService.getActiveCenters()
            );

            model.addAttribute("activePage", "classrooms");

            return "classroom-form";
        }

        classroomService.updateClassroom(id, classroom);

        return "redirect:/classrooms";
    }

    @PostMapping("/classrooms/{id}/deactivate")
    public String deactivateClassroom(@PathVariable Long id) {

        classroomService.deactivateClassroom(id);

        return "redirect:/classrooms";
    }

    private void validateAgeRange(
            Classroom classroom,
            BindingResult bindingResult) {

        Integer minimumAge = classroom.getMinimumAgeMonths();
        Integer maximumAge = classroom.getMaximumAgeMonths();

        if (minimumAge != null
                && maximumAge != null
                && maximumAge < minimumAge) {

            bindingResult.rejectValue(
                    "maximumAgeMonths",
                    "classroom.maximumAgeMonths",
                    "Maximum age must be greater than or equal to minimum age."
            );
        }
    }
}