package com.example.vermillion.Controller;

import com.example.vermillion.DTO.UserDto;
import com.example.vermillion.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Test
    void showRegisterForm_authenticated() {
        UserService userService = mock(UserService.class);
        Model model = mock(Model.class);
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("user");
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        AuthController controller = new AuthController(userService);
        String result = controller.showRegisterForm(model);

        assertEquals("redirect:/", result);
    }

    @Test
    void showRegisterForm_notAuthenticated() {
        UserService userService = mock(UserService.class);
        Model model = mock(Model.class);
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("anonymousUser");
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        AuthController controller = new AuthController(userService);
        String result = controller.showRegisterForm(model);

        assertEquals("auth/register", result);
        verify(model).addAttribute(eq("userDto"), any(UserDto.class));
    }

    @Test
    void register_hasErrors() {
        UserService userService = mock(UserService.class);
        Model model = mock(Model.class);
        BindingResult bindingResult = mock(BindingResult.class);
        UserDto userDto = new UserDto();

        when(bindingResult.hasErrors()).thenReturn(true);

        AuthController controller = new AuthController(userService);
        String result = controller.register(userDto, bindingResult, model);

        assertEquals("auth/register", result);
    }

    @Test
    void register_userExists() {
        UserService userService = mock(UserService.class);
        Model model = mock(Model.class);
        BindingResult bindingResult = mock(BindingResult.class);
        UserDto userDto = new UserDto();
        userDto.setUsername("test");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.userExists("test")).thenReturn(true);

        AuthController controller = new AuthController(userService);
        String result = controller.register(userDto, bindingResult, model);

        assertEquals("auth/register", result);
        verify(model).addAttribute("error", "Пользователь уже существует");
    }

    @Test
    void register_success() {
        UserService userService = mock(UserService.class);
        Model model = mock(Model.class);
        BindingResult bindingResult = mock(BindingResult.class);
        UserDto userDto = new UserDto();
        userDto.setUsername("test");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.userExists("test")).thenReturn(false);

        AuthController controller = new AuthController(userService);
        String result = controller.register(userDto, bindingResult, model);

        assertEquals("redirect:/login?success", result);
        verify(userService).register(userDto);
    }

    @Test
    void showLoginForm_authenticated() {
        UserService userService = mock(UserService.class);
        Model model = mock(Model.class);
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("user");
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        AuthController controller = new AuthController(userService);
        String result = controller.showLoginForm(null, null, null, model);

        assertEquals("redirect:/", result);
    }

    @Test
    void showLoginForm_withError() {
        UserService userService = mock(UserService.class);
        Model model = mock(Model.class);

        AuthController controller = new AuthController(userService);
        String result = controller.showLoginForm("error", null, null, model);

        assertEquals("auth/login", result);
        verify(model).addAttribute("error", "Неверные данные");
    }

    @Test
    void showLoginForm_withSuccess() {
        UserService userService = mock(UserService.class);
        Model model = mock(Model.class);

        AuthController controller = new AuthController(userService);
        String result = controller.showLoginForm(null, null, "success", model);

        assertEquals("auth/login", result);
        verify(model).addAttribute("msg", "Регистрация прошла успешно!");
    }
}