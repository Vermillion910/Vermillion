package com.example.vermillion.Controller;

import com.example.vermillion.DTO.RoleDto;
import com.example.vermillion.Model.Role;
import com.example.vermillion.Service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleControllerTest {

    private final RoleService roleService = mock(RoleService.class);
    private final RoleController controller = new RoleController(roleService);
    private final Model model = mock(Model.class);

    // HTML/Thymeleaf endpoints
    @Test
    void listRoles_ShouldAddAttributesAndReturnView() {
        when(roleService.findAll()).thenReturn(List.of(new Role()));
        when(roleService.findAllDevelopers()).thenReturn(List.of());

        String viewName = controller.listRoles(model);

        assertEquals("roles/list", viewName);
        verify(model).addAttribute("roles", roleService.findAll());
        verify(model).addAttribute("developers", roleService.findAllDevelopers());
    }

    @Test
    void showForm_WithoutId_ShouldAddNewRole() {
        when(roleService.findAllDevelopers()).thenReturn(List.of());

        String viewName = controller.showForm(null, model);

        assertEquals("roles/form", viewName);
        verify(model).addAttribute(eq("role"), any(Role.class));
        verify(model).addAttribute("developers", roleService.findAllDevelopers());
    }

    @Test
    void showForm_WithId_ShouldAddExistingRole() {
        Role role = new Role();
        role.setId(1L);
        when(roleService.findById(1L)).thenReturn(Optional.of(role));
        when(roleService.findAllDevelopers()).thenReturn(List.of());

        String viewName = controller.showForm(1L, model);

        assertEquals("roles/form", viewName);
        verify(model).addAttribute("role", role);
        verify(model).addAttribute("developers", roleService.findAllDevelopers());
    }

    @Test
    void saveRole_ShouldCallServiceAndRedirect() {
        Role role = new Role();

        String viewName = controller.saveRole(role);

        assertEquals("redirect:/roles", viewName);
        verify(roleService).saveEntity(role);
    }

    @Test
    void deleteRole_ShouldCallServiceAndRedirect() {
        String viewName = controller.deleteRole(1L);

        assertEquals("redirect:/roles", viewName);
        verify(roleService).delete(1L);
    }




}