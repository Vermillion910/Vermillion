package com.example.vermillion.Controller;

import com.example.vermillion.DTO.ProjectDto;
import com.example.vermillion.Model.Project;
import com.example.vermillion.Service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectControllerTest {

    @Test
    void listProjects() {
        ProjectService service = mock(ProjectService.class);
        Model model = mock(Model.class);
        when(service.findAll()).thenReturn(List.of(new Project()));

        ProjectController controller = new ProjectController(service);
        String viewName = controller.listProjects(model);

        assertEquals("projects/list", viewName);
        verify(model).addAttribute("projects", service.findAll());
    }

    @Test
    void showForm_newProject() {
        ProjectService service = mock(ProjectService.class);
        Model model = mock(Model.class);

        ProjectController controller = new ProjectController(service);
        String viewName = controller.showForm(null, model);

        assertEquals("projects/form", viewName);
        verify(model).addAttribute(eq("project"), any(Project.class));
        verify(model).addAttribute("developers", service.findAllManagers());
    }

    @Test
    void saveProject() {
        ProjectService service = mock(ProjectService.class);
        Project project = new Project();

        ProjectController controller = new ProjectController(service);
        String viewName = controller.saveProject(project);

        assertEquals("redirect:/projects", viewName);
        verify(service).saveEntity(project);
    }

    @Test
    void apiGetAll() {
        ProjectService service = mock(ProjectService.class);
        when(service.findAll()).thenReturn(List.of(new Project()));

        ProjectController controller = new ProjectController(service);
        List<Project> result = controller.apiGetAll();

        assertNotNull(result);
        verify(service).findAll();
    }

    @Test
    void apiCreate() {
        ProjectService service = mock(ProjectService.class);
        ProjectDto dto = new ProjectDto();
        Project project = new Project();
        when(service.create(dto)).thenReturn(project);

        ProjectController controller = new ProjectController(service);
        Project result = controller.apiCreate(dto);

        assertEquals(project, result);
    }
}