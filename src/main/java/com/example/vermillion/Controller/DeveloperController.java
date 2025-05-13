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

    // ====== API JSON ======

    @GetMapping("/api")
    @ResponseBody
    public List<Developer> apiGetAll() {
        return developerService.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Developer apiGetById(@PathVariable Long id) {
        return developerService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Developer not found: " + id));
    }

    @PostMapping("/api")
    @ResponseBody
    public Developer apiCreate(@RequestBody DeveloperDto dto) {
        return developerService.create(dto);
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public Developer apiUpdate(@PathVariable Long id, @RequestBody DeveloperDto dto) {
        return developerService.update(id, dto)
                .orElseThrow(() -> new IllegalArgumentException("Developer not found: " + id));
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public void apiDelete(@PathVariable Long id) {
        if (!developerService.delete(id)) {
            throw new IllegalArgumentException("Developer not found: " + id);
        }
    }

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
