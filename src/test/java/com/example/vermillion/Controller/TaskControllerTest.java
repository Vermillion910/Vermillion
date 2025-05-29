package com.example.vermillion.Controller;

import com.example.vermillion.DTO.TaskDto;
import com.example.vermillion.Model.Task;
import com.example.vermillion.Service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    private final TaskService taskService = mock(TaskService.class);
    private final TaskController controller = new TaskController(taskService);
    private final Model model = mock(Model.class);

    // HTML/Thymeleaf endpoints
    @Test
    void listTasks_ShouldAddTasksAndReturnView() {
        when(taskService.findAll()).thenReturn(List.of(new Task()));

        String viewName = controller.listTasks(model);

        assertEquals("tasks/list", viewName);
        verify(model).addAttribute("tasks", taskService.findAll());
    }

    @Test
    void showForm_WithoutId_ShouldAddNewTask() {
        when(taskService.findAllProjects()).thenReturn(List.of());
        when(taskService.findAllDevelopers()).thenReturn(List.of());

        String viewName = controller.showForm(null, model);

        assertEquals("tasks/form", viewName);
        verify(model).addAttribute(eq("task"), any(Task.class));
        verify(model).addAttribute("projects", taskService.findAllProjects());
        verify(model).addAttribute("developers", taskService.findAllDevelopers());
    }

    @Test
    void showForm_WithId_ShouldAddExistingTask() {
        Task task = new Task();
        task.setId(1L);
        when(taskService.findById(1L)).thenReturn(Optional.of(task));
        when(taskService.findAllProjects()).thenReturn(List.of());
        when(taskService.findAllDevelopers()).thenReturn(List.of());

        String viewName = controller.showForm(1L, model);

        assertEquals("tasks/form", viewName);
        verify(model).addAttribute("task", task);
        verify(model).addAttribute("projects", taskService.findAllProjects());
        verify(model).addAttribute("developers", taskService.findAllDevelopers());
    }

    @Test
    void saveTask_ShouldCallServiceAndRedirect() {
        Task task = new Task();

        String viewName = controller.saveTask(task);

        assertEquals("redirect:/tasks", viewName);
        verify(taskService).saveEntity(task);
    }

    @Test
    void deleteTask_ShouldCallServiceAndRedirect() {
        String viewName = controller.deleteTask(1L);

        assertEquals("redirect:/tasks", viewName);
        verify(taskService).delete(1L);
    }

    // API endpoints

}