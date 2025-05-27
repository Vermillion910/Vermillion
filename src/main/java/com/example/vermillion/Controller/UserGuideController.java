package com.example.vermillion.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserGuideController {

    @GetMapping("/user-guide")
    public String showUserGuide() {
        return "user-guide";
    }
} 