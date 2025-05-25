package com.example.vermillion.Controller;

import com.example.vermillion.DTO.DeveloperDto;
import com.example.vermillion.Model.Developer;
import com.example.vermillion.Service.DeveloperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты для контроллера разработчиков
 */
@SpringBootTest
@AutoConfigureMockMvc
class DeveloperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBedean
    private DeveloperService developerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("Тесты REST API")
    class ApiTests {

        @Test
        @WithMockUser
        @DisplayName("GET /api - получение списка разработчиков")
        void whenGetAllDevelopers_thenReturnsJsonArray() throws Exception {
            Developer dev1 = createDeveloper(1L, "Иван", "Иванов", "ivan@test.com");
            Developer dev2 = createDeveloper(2L, "Петр", "Петров", "petr@test.com");
            when(developerService.findAll()).thenReturn(Arrays.asList(dev1, dev2));

            mockMvc.perform(get("/developers/api"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].firstName").value("Иван"))
                    .andExpect(jsonPath("$[1].firstName").value("Петр"));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /api/{id} - получение разработчика по ID")
        void whenGetDeveloperById_thenReturnsDeveloper() throws Exception {
            Developer dev = createDeveloper(1L, "Иван", "Иванов", "ivan@test.com");
            when(developerService.findById(1L)).thenReturn(Optional.of(dev));

            mockMvc.perform(get("/developers/api/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.firstName").value("Иван"))
                    .andExpect(jsonPath("$.lastName").value("Иванов"))
                    .andExpect(jsonPath("$.email").value("ivan@test.com"));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /api/{id} - обработка несуществующего разработчика")
        void whenGetNonExistentDeveloper_thenReturns404() throws Exception {
            when(developerService.findById(anyLong())).thenReturn(Optional.empty());

            mockMvc.perform(get("/developers/api/999"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        @DisplayName("POST /api - создание нового разработчика")
        void whenCreateDeveloper_thenReturnsCreatedDeveloper() throws Exception {
            DeveloperDto devDto = new DeveloperDto();
            devDto.setFirstName("Иван");
            devDto.setLastName("Иванов");
            devDto.setEmail("ivan@test.com");
            devDto.setPhoneNumber("+7-999-999-9999");
            devDto.setSpecialization("Java Developer");

            Developer createdDev = createDeveloper(1L, "Иван", "Иванов", "ivan@test.com");
            when(developerService.create(any(DeveloperDto.class))).thenReturn(createdDev);

            mockMvc.perform(post("/developers/api")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(devDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.developerId").value(1))
                    .andExpect(jsonPath("$.firstName").value("Иван"));

            verify(developerService).create(any(DeveloperDto.class));
        }

        @Test
        @WithMockUser
        @DisplayName("PUT /api/{id} - обновление разработчика")
        void whenUpdateDeveloper_thenReturnsUpdatedDeveloper() throws Exception {
            DeveloperDto devDto = new DeveloperDto();
            devDto.setFirstName("Иван");
            devDto.setLastName("Иванович");
            devDto.setEmail("ivan.new@test.com");

            Developer updatedDev = createDeveloper(1L, "Иван", "Иванович", "ivan.new@test.com");
            when(developerService.update(anyLong(), any(DeveloperDto.class))).thenReturn(Optional.of(updatedDev));

            mockMvc.perform(put("/developers/api/1")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(devDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").value("Иван"))
                    .andExpect(jsonPath("$.lastName").value("Иванович"))
                    .andExpect(jsonPath("$.email").value("ivan.new@test.com"));
        }

        @Test
        @WithMockUser
        @DisplayName("DELETE /api/{id} - удаление разработчика")
        void whenDeleteDeveloper_thenReturnsSuccess() throws Exception {
            when(developerService.delete(anyLong())).thenReturn(true);

            mockMvc.perform(delete("/developers/api/1")
                    .with(csrf()))
                    .andExpect(status().isOk());

            verify(developerService).delete(1L);
        }
    }

    @Nested
    @DisplayName("Тесты веб-интерфейса")
    class WebInterfaceTests {

        @Test
        @WithMockUser
        @DisplayName("GET /developers - отображение списка разработчиков")
        void whenGetDevelopersPage_thenReturnsListView() throws Exception {
            Developer dev1 = createDeveloper(1L, "Иван", "Иванов", "ivan@test.com");
            Developer dev2 = createDeveloper(2L, "Петр", "Петров", "petr@test.com");
            when(developerService.findAll()).thenReturn(Arrays.asList(dev1, dev2));

            mockMvc.perform(get("/developers"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("developers/list"))
                    .andExpect(model().attributeExists("developers"));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /developers/new - форма создания разработчика")
        void whenGetNewDeveloperForm_thenReturnsForm() throws Exception {
            mockMvc.perform(get("/developers/new"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("developers/form"))
                    .andExpect(model().attributeExists("developer"));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /developers/edit/{id} - форма редактирования разработчика")
        void whenGetEditForm_thenReturnsFormWithDeveloper() throws Exception {
            Developer dev = createDeveloper(1L, "Иван", "Иванов", "ivan@test.com");
            when(developerService.findById(1L)).thenReturn(Optional.of(dev));

            mockMvc.perform(get("/developers/edit/1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("developers/form"))
                    .andExpect(model().attributeExists("developer"));
        }
    }

    @Nested
    @DisplayName("Тесты безопасности")
    class SecurityTests {

        @Test
        @DisplayName("Доступ к API без аутентификации")
        void whenNotAuthenticated_thenRedirectToLogin() throws Exception {
            mockMvc.perform(get("/developers/api"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("http://localhost/login"));
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("Доступ к API с ролью USER")
        void whenAuthenticatedAsUser_thenAllowed() throws Exception {
            mockMvc.perform(get("/developers/api"))
                    .andExpect(status().isOk());
        }
    }

    private Developer createDeveloper(Long id, String firstName, String lastName, String email) {
        Developer dev = new Developer();
        dev.setDeveloperId(id);
        dev.setFirstName(firstName);
        dev.setLastName(lastName);
        dev.setEmail(email);
        return dev;
    }
} 