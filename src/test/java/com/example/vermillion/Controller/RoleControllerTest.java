package com.example.vermillion.Controller;

import com.example.vermillion.DTO.RoleDto;
import com.example.vermillion.Model.Role;
import com.example.vermillion.Service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoleController.class)
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllRoles() throws Exception {
        // Подготавливаем тестовые данные
        Role role1 = new Role();
        role1.setId(1L);
        role1.setRoleName("Developer");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setRoleName("Manager");

        when(roleService.findAll()).thenReturn(Arrays.asList(role1, role2));

        // Проверяем API метод
        mockMvc.perform(get("/roles/api"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].roleName").value("Developer"))
                .andExpect(jsonPath("$[1].roleName").value("Manager"));
    }

    @Test
    public void testCreateRole() throws Exception {
        // Подготавливаем тестовые данные
        RoleDto roleDto = new RoleDto();
        roleDto.setRoleName("Developer");
        roleDto.setDeveloperId(1L);

        Role createdRole = new Role();
        createdRole.setId(1L);
        createdRole.setRoleName("Developer");

        when(roleService.create(any(RoleDto.class))).thenReturn(createdRole);

        // Проверяем создание роли через API
        mockMvc.perform(post("/roles/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.roleName").value("Developer"));
    }

    @Test
    public void testDeleteRole() throws Exception {
        when(roleService.delete(1L)).thenReturn(true);

        // Проверяем удаление роли
        mockMvc.perform(delete("/roles/api/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testShowRolesList() throws Exception {
        // Проверяем отображение списка ролей
        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(view().name("roles/list"));
    }
} 