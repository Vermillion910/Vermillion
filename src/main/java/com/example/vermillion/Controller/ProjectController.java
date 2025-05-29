package com.example.vermillion.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.vermillion.DTO.ProjectDto;
import com.example.vermillion.Model.Project;
import com.example.vermillion.Service.ProjectService;

import java.util.List;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // ========== API (JSON) ==========



    // ========== HTML (Thymeleaf) ==========

    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.findAll());
        return "projects/list";
    }

    @GetMapping("/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            projectService.findById(id).ifPresent(p -> model.addAttribute("project", p));
        } else {
            model.addAttribute("project", new Project());
        }
        // Если нужна выпадашка менеджеров:
        model.addAttribute("developers", projectService.findAllManagers());
        return "projects/form";
    }

    @PostMapping("/save")
    public String saveProject(@ModelAttribute Project project) {
        // здесь можно маппить из ProjectDto или строить Project напрямую
        projectService.saveEntity(project);
        return "redirect:/projects";
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return "redirect:/projects";
    }
}
