package com.example.vermillion.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RoleDto {
    private Long id;

    @NotNull(message = "ID разработчика обязателен")
    private Long developerId;

    @NotBlank(message = "Название роли обязательно")
    @Size(max = 255)
    private String roleName;
}