package com.example.vermillion.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Сущность проекта
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(nullable = false, length = 255)
    private String projectName;

    @ManyToOne
    @JoinColumn(name = "projectManagerId", nullable = false)
    private Developer projectManager;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private BigDecimal budget;
}