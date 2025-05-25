package com.example.vermillion.Controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для контроллера главной страницы
 */
@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("Тесты доступа к главной странице")
    class HomePageAccessTests {
        
        @Test
        @DisplayName("GET / без аутентификации перенаправляет на страницу входа")
        void whenNotAuthenticated_thenRedirectToLogin() throws Exception {
            mockMvc.perform(get("/"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("http://localhost/login"));
        }

        @Test
        @WithMockUser
        @DisplayName("GET / с аутентификацией возвращает главную страницу")
        void whenAuthenticated_thenReturnsHomePage() throws Exception {
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"))
                    .andExpect(content().contentType("text/html;charset=UTF-8"));
        }
    }

    @Nested
    @DisplayName("Тесты обработки ошибок")
    class ErrorHandlingTests {

        @Test
        @WithMockUser
        @DisplayName("GET /invalid-url возвращает 404")
        void whenInvalidUrl_thenReturns404() throws Exception {
            mockMvc.perform(get("/invalid-url"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        @DisplayName("Обработка системных ошибок возвращает страницу ошибки")
        void whenSystemError_thenReturnsErrorPage() throws Exception {
            mockMvc.perform(get("/error"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"));
        }
    }
} 