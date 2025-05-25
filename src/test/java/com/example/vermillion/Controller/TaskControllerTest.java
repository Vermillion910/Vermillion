package com.example.vermillion.Controller;

import com.example.vermillion.DTO.TaskDto;
import com.example.vermillion.Model.Task;
import com.example.vermillion.Service.TaskService;
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
 * Тесты для контроллера задач
 */
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("Тесты REST API")
    class ApiTests {

        @Test
        @WithMockUser
        @DisplayName("GET /api - получение списка задач")
        void whenGetAllTasks_thenReturnsJsonArray() throws Exception {
            Task task1 = createTask(1L, "Задача 1", "Описание 1", "В работе");
            Task task2 = createTask(2L, "Задача 2", "Описание 2", "Новая");
            when(taskService.findAll()).thenReturn(Arrays.asList(task1, task2));

            mockMvc.perform(get("/tasks/api"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].taskName").value("Задача 1"))
                    .andExpect(jsonPath("$[1].taskName").value("Задача 2"));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /api/{id} - получение задачи по ID")
        void whenGetTaskById_thenReturnsTask() throws Exception {
            Task task = createTask(1L, "Тестовая задача", "Описание задачи", "В работе");
            when(taskService.findById(1L)).thenReturn(Optional.of(task));

            mockMvc.perform(get("/tasks/api/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.taskName").value("Тестовая задача"))
                    .andExpect(jsonPath("$.description").value("Описание задачи"))
                    .andExpect(jsonPath("$.status").value("В работе"));
        }

        @Test
        @WithMockUser
        @DisplayName("POST /api - создание новой задачи")
        void whenCreateTask_thenReturnsCreatedTask() throws Exception {
            TaskDto taskDto = new TaskDto();
            taskDto.setTaskName("Новая задача");
            taskDto.setDescription("Описание новой задачи");
            taskDto.setStatus("В работе");
            taskDto.setProjectId(1L);
            taskDto.setAssignedToId(1L);
            taskDto.setDueDate(LocalDate.now().plusDays(7));

            Task createdTask = createTask(1L, "Новая задача", "Описание новой задачи", "В работе");
            when(taskService.create(any(TaskDto.class))).thenReturn(createdTask);

            mockMvc.perform(post("/tasks/api")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.taskName").value("Новая задача"));

            verify(taskService).create(any(TaskDto.class));
        }

        @Test
        @WithMockUser
        @DisplayName("PUT /api/{id} - обновление задачи")
        void whenUpdateTask_thenReturnsUpdatedTask() throws Exception {
            TaskDto taskDto = new TaskDto();
            taskDto.setTaskName("Обновленная задача");
            taskDto.setDescription("Обновленное описание");
            taskDto.setStatus("Завершена");
            taskDto.setDueDate(LocalDate.now().plusDays(14));

            Task updatedTask = createTask(1L, "Обновленная задача", "Обновленное описание", "Завершена");
            when(taskService.update(anyLong(), any(TaskDto.class))).thenReturn(Optional.of(updatedTask));

            mockMvc.perform(put("/tasks/api/1")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.taskName").value("Обновленная задача"))
                    .andExpect(jsonPath("$.status").value("Завершена"));
        }

        @Test
        @WithMockUser
        @DisplayName("DELETE /api/{id} - удаление задачи")
        void whenDeleteTask_thenReturnsSuccess() throws Exception {
            when(taskService.delete(anyLong())).thenReturn(true);

            mockMvc.perform(delete("/tasks/api/1")
                    .with(csrf()))
                    .andExpect(status().isOk());

            verify(taskService).delete(1L);
        }
    }

    @Nested
    @DisplayName("Тесты веб-интерфейса")
    class WebInterfaceTests {

        @Test
        @WithMockUser
        @DisplayName("GET /tasks - отображение списка задач")
        void whenGetTasksPage_thenReturnsListView() throws Exception {
            Task task1 = createTask(1L, "Задача 1", "Описание 1", "В работе");
            Task task2 = createTask(2L, "Задача 2", "Описание 2", "Новая");
            when(taskService.findAll()).thenReturn(Arrays.asList(task1, task2));

            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("tasks/list"))
                    .andExpect(model().attributeExists("tasks"));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /tasks/new - форма создания задачи")
        void whenGetNewTaskForm_thenReturnsForm() throws Exception {
            mockMvc.perform(get("/tasks/new"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("tasks/form"))
                    .andExpect(model().attributeExists("task"));
        }

        @Test
        @WithMockUser
        @DisplayName("GET /tasks/edit/{id} - форма редактирования задачи")
        void whenGetEditForm_thenReturnsFormWithTask() throws Exception {
            Task task = createTask(1L, "Тестовая задача", "Описание задачи", "В работе");
            when(taskService.findById(1L)).thenReturn(Optional.of(task));

            mockMvc.perform(get("/tasks/edit/1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("tasks/form"))
                    .andExpect(model().attributeExists("task"));
        }
    }

    @Nested
    @DisplayName("Тесты безопасности")
    class SecurityTests {

        @Test
        @DisplayName("Доступ к API без аутентификации")
        void whenNotAuthenticated_thenRedirectToLogin() throws Exception {
            mockMvc.perform(get("/tasks/api"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("http://localhost/login"));
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("Доступ к API с ролью USER")
        void whenAuthenticatedAsUser_thenAllowed() throws Exception {
            mockMvc.perform(get("/tasks/api"))
                    .andExpect(status().isOk());
        }
    }

    private Task createTask(Long id, String name, String description, String status) {
        Task task = new Task();
        task.setId(id);
        task.setTaskName(name);
        task.setDescription(description);
        task.setStatus(status);
        task.setDueDate(LocalDate.now().plusDays(7));
        return task;
    }
} 