package com.example.vermillion.Controller;

import com.example.vermillion.DTO.DeveloperDto;
import com.example.vermillion.Model.Developer;
import com.example.vermillion.Service.DeveloperService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeveloperControllerTest {

    @Test
    void listDevelopers() {
        DeveloperService service = mock(DeveloperService.class);
        Model model = mock(Model.class);
        when(service.findAll()).thenReturn(List.of(new Developer()));

        DeveloperController controller = new DeveloperController(service);
        String viewName = controller.listDevelopers(model);

        assertEquals("developers/list", viewName);
        verify(model).addAttribute("developers", service.findAll());
    }

    @Test
    void showForm_newDeveloper() {
        DeveloperService service = mock(DeveloperService.class);
        Model model = mock(Model.class);

        DeveloperController controller = new DeveloperController(service);
        String viewName = controller.showForm(null, model);

        assertEquals("developers/form", viewName);
        verify(model).addAttribute(eq("developer"), any(Developer.class));
    }

    @Test
    void showForm_existingDeveloper() {
        DeveloperService service = mock(DeveloperService.class);
        Model model = mock(Model.class);
        Developer dev = new Developer();
        when(service.findById(1L)).thenReturn(Optional.of(dev));

        DeveloperController controller = new DeveloperController(service);
        String viewName = controller.showForm(1L, model);

        assertEquals("developers/form", viewName);
        verify(model).addAttribute("developer", dev);
    }

    @Test
    void saveDeveloper() {
        DeveloperService service = mock(DeveloperService.class);
        Developer developer = new Developer();

        DeveloperController controller = new DeveloperController(service);
        String viewName = controller.saveDeveloper(developer);

        assertEquals("redirect:/developers", viewName);
        verify(service).saveEntity(developer);
    }

    @Test
    void deleteDeveloper() {
        DeveloperService service = mock(DeveloperService.class);

        DeveloperController controller = new DeveloperController(service);
        String viewName = controller.deleteDeveloper(1L);

        assertEquals("redirect:/developers", viewName);
        verify(service).delete(1L);
    }

    @Test
    void apiGetAll() {
        DeveloperService service = mock(DeveloperService.class);
        when(service.findAll()).thenReturn(List.of(new Developer()));

        DeveloperController controller = new DeveloperController(service);
        List<Developer> result = controller.apiGetAll();

        assertNotNull(result);
        verify(service).findAll();
    }

    @Test
    void apiGetById() {
        DeveloperService service = mock(DeveloperService.class);
        Developer dev = new Developer();
        when(service.findById(1L)).thenReturn(Optional.of(dev));

        DeveloperController controller = new DeveloperController(service);
        Developer result = controller.apiGetById(1L);

        assertEquals(dev, result);
    }

    @Test
    void apiCreate() {
        DeveloperService service = mock(DeveloperService.class);
        DeveloperDto dto = new DeveloperDto();
        Developer dev = new Developer();
        when(service.create(dto)).thenReturn(dev);

        DeveloperController controller = new DeveloperController(service);
        Developer result = controller.apiCreate(dto);

        assertEquals(dev, result);
    }

    @Test
    void apiUpdate() {
        DeveloperService service = mock(DeveloperService.class);
        DeveloperDto dto = new DeveloperDto();
        Developer dev = new Developer();
        when(service.update(1L, dto)).thenReturn(Optional.of(dev));

        DeveloperController controller = new DeveloperController(service);
        Developer result = controller.apiUpdate(1L, dto);

        assertEquals(dev, result);
    }

    @Test
    void apiDelete() {
        DeveloperService service = mock(DeveloperService.class);

        DeveloperController controller = new DeveloperController(service);
        controller.apiDelete(1L);

        verify(service).delete(1L);
    }
}