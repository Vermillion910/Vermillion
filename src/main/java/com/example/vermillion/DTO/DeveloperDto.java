package com.example.vermillion.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DeveloperDto {
    private Long developerId;

    @NotBlank(message = "Имя обязательно")
    @Size(max = 255)
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    @Size(max = 255)
    private String lastName;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    @Size(max = 255)
    private String email;

    @Size(max = 20)
    private String phoneNumber;

    @Size(max = 255)
    private String specialization;
}

