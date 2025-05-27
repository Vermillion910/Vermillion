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

    // API endpoints
    @Test
    void apiGetAll_ShouldReturnAllRoles() {
        List<Role> roles = List.of(new Role());
        when(roleService.findAll()).thenReturn(roles);

        List<Role> result = controller.apiGetAll();

        assertEquals(roles, result);
    }

    @Test
    void apiGetById_ShouldReturnRole() {
        Role role = new Role();
        when(roleService.findById(1L)).thenReturn(Optional.of(role));

        Role result = controller.apiGetById(1L);

        assertEquals(role, result);
    }

    @Test
    void apiCreate_ShouldReturnCreatedRole() {
        RoleDto dto = new RoleDto();
        Role role = new Role();
        when(roleService.create(dto)).thenReturn(role);

        Role result = controller.apiCreate(dto);

        assertEquals(role, result);
    }

    @Test
    void apiUpdate_ShouldReturnUpdatedRole() {
        RoleDto dto = new RoleDto();
        Role role = new Role();
        when(roleService.update(1L, dto)).thenReturn(Optional.of(role));

        Role result = controller.apiUpdate(1L, dto);

        assertEquals(role, result);
    }

    @Test
    void apiDelete_ShouldCallServiceAndNotThrowWhenRoleExists() {
        when(roleService.delete(1L)).thenReturn(true);

        assertDoesNotThrow(() -> controller.apiDelete(1L));
        verify(roleService).delete(1L);
    }

    @Test
    void apiDelete_ShouldThrowWhenRoleNotFound() {
        when(roleService.delete(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> controller.apiDelete(1L));

        assertEquals("Role not found: 1", exception.getMessage());
        verify(roleService).delete(1L);
    }
}