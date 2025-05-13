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

    @GetMapping("/api")
    @ResponseBody
    public List<Project> apiGetAll() {
        return projectService.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Project apiGetById(@PathVariable Long id) {
        return projectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }

    @PostMapping("/api")
    @ResponseBody
    public Project apiCreate(@RequestBody ProjectDto dto) {
        return projectService.create(dto);
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public Project apiUpdate(@PathVariable Long id, @RequestBody ProjectDto dto) {
        return projectService.update(id, dto)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public void apiDelete(@PathVariable Long id) {
        if (!projectService.delete(id)) {
            throw new IllegalArgumentException("Project not found");
        }
    }

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
