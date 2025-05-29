package com.example.vermillion.Controller;

import com.example.vermillion.DTO.UserDto;
import com.example.vermillion.Model.User;
import com.example.vermillion.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserManagementController {
    private final UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        userService.findById(id).ifPresent(user -> {
            UserDto userDto = new UserDto();
            userDto.setUsername(user.getUsername());
            userDto.setRole(user.getRole());
            model.addAttribute("userDto", userDto);
            model.addAttribute("userId", id);
        });
        return "admin/users/edit";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(id, userDto);
            redirectAttributes.addFlashAttribute("success", "Пользователь успешно обновлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении пользователя: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "Пользователь успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении пользователя: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
} 