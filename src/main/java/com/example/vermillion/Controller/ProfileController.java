package com.example.vermillion.Controller;

import com.example.vermillion.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("username") String newUsername,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        String currentUsername = authentication.getName();
        
        if (currentUsername.equals(newUsername)) {
            return "redirect:/";
        }

        if (userService.userExists(newUsername)) {
            redirectAttributes.addFlashAttribute("error", "Пользователь с таким именем уже существует");
            return "redirect:/";
        }

        userService.updateUsername(currentUsername, newUsername);
        redirectAttributes.addFlashAttribute("success", "Имя пользователя успешно обновлено");
        return "redirect:/login";
    }
} 