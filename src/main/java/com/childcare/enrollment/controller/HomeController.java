package com.childcare.enrollment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.childcare.enrollment.service.DashboardService;

@Controller
public class HomeController {

    private final DashboardService dashboardService;

    public HomeController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute(
                "dashboard",
                dashboardService.getDashboardSummary()
        );

        model.addAttribute("activePage", "dashboard");

        return "index";
    }
}