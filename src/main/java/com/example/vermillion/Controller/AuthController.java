package com.example.vermillion.Controller;

import com.example.vermillion.DTO.UserDto;
import com.example.vermillion.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userDto") UserDto userDto,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        if (userService.userExists(userDto.getUsername())) {
            model.addAttribute("error", "Пользователь уже существует");
            return "auth/register";
        }

        userService.register(userDto);
        return "redirect:/login?success";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String error,
                                @RequestParam(required = false) String logout,
                                @RequestParam(required = false) String success,
                                Model model) {
        if (error != null) model.addAttribute("error", "Неверные данные");
        if (logout != null) model.addAttribute("msg", "Вы вышли");
        if (success != null) model.addAttribute("msg", "Регистрация прошла успешно!");
        return "auth/login";
    }
}