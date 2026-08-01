package com.childcare.enrollment.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.childcare.enrollment.engine.EnrollmentProjectionEngine;
import com.childcare.enrollment.engine.ProjectionResult;

@Controller
public class FutureEnrollmentController {

    private final EnrollmentProjectionEngine projectionEngine;

    public FutureEnrollmentController(
            EnrollmentProjectionEngine projectionEngine) {

        this.projectionEngine = projectionEngine;
    }

    @GetMapping("/future-enrollment")
    public String viewFutureEnrollment(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate projectionDate,
            Model model) {

        model.addAttribute("activePage", "future");

        if (projectionDate == null) {
            projectionDate = LocalDate.now().plusMonths(1);
        }

        ProjectionResult projection =
                projectionEngine.createProjection(projectionDate);

        model.addAttribute("projectionDate", projectionDate);
        model.addAttribute("projection", projection);

        return "future-enrollment";
    }
}