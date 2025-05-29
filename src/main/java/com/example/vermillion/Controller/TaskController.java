package com.example.vermillion.Controller;

import com.example.vermillion.DTO.TaskDto;
import com.example.vermillion.Model.Task;
import com.example.vermillion.Service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // ====== API JSON ======



    // ====== HTML/Thymeleaf ======

    @GetMapping
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            taskService.findById(id).ifPresent(t -> model.addAttribute("task", t));
        } else {
            model.addAttribute("task", new Task());
        }
        model.addAttribute("projects", taskService.findAllProjects());
        model.addAttribute("developers", taskService.findAllDevelopers());
        return "tasks/form";
    }

    @PostMapping("/save")
    public String saveTask(@ModelAttribute Task task) {
        taskService.saveEntity(task);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }
}
