package com.example.vermillion.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GuideController {

    @GetMapping("/guide")
    public String showGuide() {
        return "guide";
    }
} 