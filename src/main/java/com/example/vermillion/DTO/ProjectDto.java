package com.example.vermillion.DTO;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ProjectDto {

    @NotBlank(message = "Название проекта не может быть пустым")
    @Size(max = 255, message = "Максимум 255 символов")
    private String projectName;

    @NotNull(message = "Нужно указать менеджера (id)")
    private Long projectManagerId;

    @NotNull(message = "Дата начала обязательна")
    @PastOrPresent(message = "Дата начала не может быть в будущем")
    private LocalDate startDate;

    @FutureOrPresent(message = "Планируемая дата окончания не может быть в прошлом")
    private LocalDate endDate;

    @NotNull(message = "Бюджет обязателен")
    @DecimalMin(value = "0.0", inclusive = false, message = "Бюджет должен быть > 0")
    private BigDecimal budget;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getProjectManagerId() {
        return projectManagerId;
    }

    public void setProjectManagerId(Long projectManagerId) {
        this.projectManagerId = projectManagerId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
}