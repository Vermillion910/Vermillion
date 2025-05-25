package com.example.vermillion.Controller;

import com.example.vermillion.DTO.UserDto;
import com.example.vermillion.Service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для контроллера аутентификации
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Nested
    @DisplayName("Тесты страницы входа")
    class LoginTests {

        @Test
        @WithAnonymousUser
        @DisplayName("GET /login - отображение формы входа")
        void whenGetLoginPage_thenReturnsLoginForm() throws Exception {
            mockMvc.perform(get("/login"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("auth/login"));
        }

        @Test
        @WithAnonymousUser
        @DisplayName("GET /login?error - отображение ошибки входа")
        void whenLoginError_thenShowsErrorMessage() throws Exception {
            mockMvc.perform(get("/login")
                    .param("error", ""))
                    .andExpect(status().isOk())
                    .andExpect(view().name("auth/login"))
                    .andExpect(model().attribute("error", "Неверные данные"));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /login для авторизованного пользователя")
        void whenAuthenticatedAccessLogin_thenRedirectToHome() throws Exception {
            mockMvc.perform(get("/login"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"));
        }
    }

    @Nested
    @DisplayName("Тесты регистрации")
    class RegistrationTests {

        @Test
        @WithAnonymousUser
        @DisplayName("GET /register - отображение формы регистрации")
        void whenGetRegisterPage_thenReturnsRegisterForm() throws Exception {
            mockMvc.perform(get("/register"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("auth/register"))
                    .andExpect(model().attributeExists("userDto"));
        }

        @Test
        @WithAnonymousUser
        @DisplayName("POST /register - успешная регистрация")
        void whenValidRegistration_thenRedirectToLogin() throws Exception {
            when(userService.userExists(anyString())).thenReturn(false);
            doNothing().when(userService).register(any(UserDto.class));

            mockMvc.perform(post("/register")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("username", "testuser")
                    .param("password", "password123"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login?success"));

            verify(userService).register(any(UserDto.class));
        }

        @Test
        @WithAnonymousUser
        @DisplayName("POST /register - ошибка валидации")
        void whenInvalidRegistration_thenReturnsFormWithErrors() throws Exception {
            mockMvc.perform(post("/register")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("username", "") // Пустое имя пользователя
                    .param("password", "123")) // Слишком короткий пароль
                    .andExpect(status().isOk())
                    .andExpect(view().name("auth/register"))
                    .andExpect(model().hasErrors());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("POST /register - пользователь уже существует")
        void whenUserExists_thenReturnsError() throws Exception {
            when(userService.userExists(anyString())).thenReturn(true);

            mockMvc.perform(post("/register")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("username", "existinguser")
                    .param("password", "password123"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("auth/register"))
                    .andExpect(model().attributeExists("error"));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /register для авторизованного пользователя")
        void whenAuthenticatedAccessRegister_thenRedirectToHome() throws Exception {
            mockMvc.perform(get("/register"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/"));
        }
    }

    @Nested
    @DisplayName("Тесты безопасности")
    class SecurityTests {

        @Test
        @WithAnonymousUser
        @DisplayName("Доступ к защищенным ресурсам без аутентификации")
        void whenAnonymousAccessSecuredEndpoint_thenRedirectToLogin() throws Exception {
            mockMvc.perform(get("/developers"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("http://localhost/login"));
        }

        @Test
        @WithMockUser
        @DisplayName("Доступ к защищенным ресурсам с аутентификацией")
        void whenAuthenticatedAccessSecuredEndpoint_thenSucceeds() throws Exception {
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk());
        }
    }
} 