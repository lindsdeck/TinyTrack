package com.childcare.enrollment.controller;

import com.childcare.enrollment.service.CenterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CenterController {

    private final CenterService centerService;

    public CenterController(CenterService centerService) {
        this.centerService = centerService;
    }

    @GetMapping("/centers")
    public String viewCenters(Model model) {

        model.addAttribute("centers",
                centerService.getActiveCenters());

        return "centers";
    }

}