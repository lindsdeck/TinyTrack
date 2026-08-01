package com.childcare.enrollment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.childcare.enrollment.model.Student;
import com.childcare.enrollment.service.StudentService;

import jakarta.validation.Valid;

@Controller
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public String viewStudents(Model model) {

        model.addAttribute(
                "students",
                studentService.getActiveStudents()
        );

        model.addAttribute("activePage", "students");

        return "students";
    }

    @GetMapping("/students/new")
    public String showAddStudentForm(Model model) {

        model.addAttribute("student", new Student());
        model.addAttribute("activePage", "students");

        return "student-form";
    }

    @PostMapping("/students/save")
    public String saveStudent(
            @Valid @ModelAttribute("student") Student student,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "students");
            return "student-form";
        }

        studentService.saveStudent(student);

        return "redirect:/students";
    }

    @GetMapping("/students/{id}/edit")
    public String showEditStudentForm(
            @PathVariable Long id,
            Model model) {

        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student not found with ID: " + id
                ));

        model.addAttribute("student", student);
        model.addAttribute("activePage", "students");

        return "student-form";
    }

    @PostMapping("/students/{id}/update")
    public String updateStudent(
            @PathVariable Long id,
            @Valid @ModelAttribute("student") Student student,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            student.setId(id);
            model.addAttribute("activePage", "students");
            return "student-form";
        }

        studentService.updateStudent(id, student);

        return "redirect:/students";
    }

    @PostMapping("/students/{id}/deactivate")
    public String deactivateStudent(@PathVariable Long id) {

        studentService.deactivateStudent(id);

        return "redirect:/students";
    }
}