package com.example.vermillion.Controller;

import com.example.vermillion.DTO.ProjectDto;
import com.example.vermillion.Model.Project;
import com.example.vermillion.Service.ProjectService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
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
 * Тесты для контроллера проектов
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("Тесты REST API")
    class ApiTests {

        @Test
        @WithMockUser
        @DisplayName("GET /api - получение списка проектов")
        void whenGetAllProjects_thenReturnsJsonArray() throws Exception {
            Project proj1 = createProject(1L, "Проект 1", LocalDate.now(), new BigDecimal("100000"));
            Project proj2 = createProject(2L, "Проект 2", LocalDate.now().plusDays(1), new BigDecimal("200000"));
            when(projectService.findAll()).thenReturn(Arrays.asList(proj1, proj2));

            mockMvc.perform(get("/projects/api"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].projectName").value("Проект 1"))
                    .andExpect(jsonPath("$[1].projectName").value("Проект 2"));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /api/{id} - получение проекта по ID")
        void whenGetProjectById_thenReturnsProject() throws Exception {
            Project proj = createProject(1L, "Тестовый проект", LocalDate.now(), new BigDecimal("100000"));
            when(projectService.findById(1L)).thenReturn(Optional.of(proj));

            mockMvc.perform(get("/projects/api/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.projectName").value("Тестовый проект"))
                    .andExpect(jsonPath("$.budget").value("100000"));
        }

        @Test
        @WithMockUser
        @DisplayName("POST /api - создание нового проекта")
        void whenCreateProject_thenReturnsCreatedProject() throws Exception {
            ProjectDto projDto = new ProjectDto();
            projDto.setProjectName("Новый проект");
            projDto.setStartDate(LocalDate.now());
            projDto.setBudget(new BigDecimal("100000"));

            Project createdProj = createProject(1L, "Новый проект", LocalDate.now(), new BigDecimal("100000"));
            when(projectService.create(any(ProjectDto.class))).thenReturn(createdProj);

            mockMvc.perform(post("/projects/api")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(projDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.projectId").value(1))
                    .andExpect(jsonPath("$.projectName").value("Новый проект"));

            verify(projectService).create(any(ProjectDto.class));
        }

        @Test
        @WithMockUser
        @DisplayName("PUT /api/{id} - обновление проекта")
        void whenUpdateProject_thenReturnsUpdatedProject() throws Exception {
            ProjectDto projDto = new ProjectDto();
            projDto.setProjectName("Обновленный проект");
            projDto.setStartDate(LocalDate.now());
            projDto.setBudget(new BigDecimal("150000"));

            Project updatedProj = createProject(1L, "Обновленный проект", LocalDate.now(), new BigDecimal("150000"));
            when(projectService.update(anyLong(), any(ProjectDto.class))).thenReturn(Optional.of(updatedProj));

            mockMvc.perform(put("/projects/api/1")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(projDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.projectName").value("Обновленный проект"))
                    .andExpect(jsonPath("$.budget").value("150000"));
        }

        @Test
        @WithMockUser
        @DisplayName("DELETE /api/{id} - удаление проекта")
        void whenDeleteProject_thenReturnsSuccess() throws Exception {
            when(projectService.delete(anyLong())).thenReturn(true);

            mockMvc.perform(delete("/projects/api/1")
                    .with(csrf()))
                    .andExpect(status().isOk());

            verify(projectService).delete(1L);
        }
    }

    @Nested
    @DisplayName("Тесты веб-интерфейса")
    class WebInterfaceTests {

        @Test
        @WithMockUser
        @DisplayName("GET /projects - отображение списка проектов")
        void whenGetProjectsPage_thenReturnsListView() throws Exception {
            Project proj1 = createProject(1L, "Проект 1", LocalDate.now(), new BigDecimal("100000"));
            Project proj2 = createProject(2L, "Проект 2", LocalDate.now().plusDays(1), new BigDecimal("200000"));
            when(projectService.findAll()).thenReturn(Arrays.asList(proj1, proj2));

            mockMvc.perform(get("/projects"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("projects/list"))
                    .andExpect(model().attributeExists("projects"));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /projects/new - форма создания проекта")
        void whenGetNewProjectForm_thenReturnsForm() throws Exception {
            mockMvc.perform(get("/projects/new"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("projects/form"))
                    .andExpect(model().attributeExists("project"));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /projects/edit/{id} - форма редактирования проекта")
        void whenGetEditForm_thenReturnsFormWithProject() throws Exception {
            Project proj = createProject(1L, "Тестовый проект", LocalDate.now(), new BigDecimal("100000"));
            when(projectService.findById(1L)).thenReturn(Optional.of(proj));

            mockMvc.perform(get("/projects/edit/1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("projects/form"))
                    .andExpect(model().attributeExists("project"));
        }
    }

    @Nested
    @DisplayName("Тесты безопасности")
    class SecurityTests {

        @Test
        @DisplayName("Доступ к API без аутентификации")
        void whenNotAuthenticated_thenRedirectToLogin() throws Exception {
            mockMvc.perform(get("/projects/api"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("http://localhost/login"));
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("Доступ к API с ролью USER")
        void whenAuthenticatedAsUser_thenAllowed() throws Exception {
            mockMvc.perform(get("/projects/api"))
                    .andExpect(status().isOk());
        }
    }

    private Project createProject(Long id, String name, LocalDate startDate, BigDecimal budget) {
        Project proj = new Project();
        proj.setProjectId(id);
        proj.setProjectName(name);
        proj.setStartDate(startDate);
        proj.setBudget(budget);
        return proj;
    }
} 