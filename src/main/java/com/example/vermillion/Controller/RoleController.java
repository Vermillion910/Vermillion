package com.example.vermillion.Controller;

import com.example.vermillion.DTO.RoleDto;
import com.example.vermillion.Model.Role;
import com.example.vermillion.Service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // ====== API JSON ======

    @GetMapping("/api")
    @ResponseBody
    public List<Role> apiGetAll() {
        return roleService.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Role apiGetById(@PathVariable Long id) {
        return roleService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + id));
    }

    @PostMapping("/api")
    @ResponseBody
    public Role apiCreate(@RequestBody RoleDto dto) {
        return roleService.create(dto);
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public Role apiUpdate(@PathVariable Long id, @RequestBody RoleDto dto) {
        return roleService.update(id, dto)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + id));
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public void apiDelete(@PathVariable Long id) {
        if (!roleService.delete(id)) {
            throw new IllegalArgumentException("Role not found: " + id);
        }
    }

    // ====== HTML/Thymeleaf ======

    @GetMapping
    public String listRoles(Model model) {
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("developers", roleService.findAllDevelopers());
        return "roles/list";
    }

    @GetMapping("/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            roleService.findById(id).ifPresent(r -> model.addAttribute("role", r));
        } else {
            model.addAttribute("role", new Role());
        }
        model.addAttribute("developers", roleService.findAllDevelopers());
        return "roles/form";
    }

    @PostMapping("/save")
    public String saveRole(@ModelAttribute Role role) {
        roleService.saveEntity(role);
        return "redirect:/roles";
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable Long id) {
        roleService.delete(id);
        return "redirect:/roles";
    }
}
