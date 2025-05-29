package com.example.vermillion.Controller;

import com.example.vermillion.DTO.DeveloperDto;
import com.example.vermillion.Model.Developer;
import com.example.vermillion.Service.DeveloperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/developers")
@RequiredArgsConstructor
public class DeveloperController {

    private final DeveloperService developerService;


    // ====== HTML/Thymeleaf ======

    @GetMapping
    public String listDevelopers(Model model) {
        model.addAttribute("developers", developerService.findAll());
        return "developers/list";
    }

    @GetMapping("/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            developerService.findById(id).ifPresent(d -> model.addAttribute("developer", d));
        } else {
            model.addAttribute("developer", new Developer());
        }
        return "developers/form";
    }

    @PostMapping("/save")
    public String saveDeveloper(@ModelAttribute Developer developer) {
        developerService.saveEntity(developer);
        return "redirect:/developers";
    }

    @GetMapping("/delete/{id}")
    public String deleteDeveloper(@PathVariable Long id) {
        developerService.delete(id);
        return "redirect:/developers";
    }
}
