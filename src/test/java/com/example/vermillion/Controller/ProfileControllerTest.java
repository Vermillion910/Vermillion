package com.example.vermillion.Controller;

import com.example.vermillion.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileControllerTest {

    @Test
    void updateProfile_sameUsername() {
        UserService service = mock(UserService.class);
        Authentication auth = mock(Authentication.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(auth.getName()).thenReturn("test");

        ProfileController controller = new ProfileController(service);
        String viewName = controller.updateProfile("test", auth, redirectAttributes);

        assertEquals("redirect:/", viewName);
        verify(service, never()).updateUsername(any(), any());
    }

    @Test
    void updateProfile_usernameExists() {
        UserService service = mock(UserService.class);
        Authentication auth = mock(Authentication.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(auth.getName()).thenReturn("test");
        when(service.userExists("new")).thenReturn(true);

        ProfileController controller = new ProfileController(service);
        String viewName = controller.updateProfile("new", auth, redirectAttributes);

        assertEquals("redirect:/", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Пользователь с таким именем уже существует");
    }

    @Test
    void updateProfile_success() {
        UserService service = mock(UserService.class);
        Authentication auth = mock(Authentication.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(auth.getName()).thenReturn("test");
        when(service.userExists("new")).thenReturn(false);

        ProfileController controller = new ProfileController(service);
        String viewName = controller.updateProfile("new", auth, redirectAttributes);

        assertEquals("redirect:/login", viewName);
        verify(service).updateUsername("test", "new");
        verify(redirectAttributes).addFlashAttribute("success", "Имя пользователя успешно обновлено");
    }
}