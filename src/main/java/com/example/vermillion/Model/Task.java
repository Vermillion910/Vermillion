package com.example.vermillion.Model;


import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, length = 255)
    private String taskName;

    @ManyToOne
    @JoinColumn(name = "assignedTo", nullable = false)
    private Developer assignedTo;

    @Column(nullable = false, length = 50)
    private String status;

    private LocalDate dueDate;

    @Column(columnDefinition = "TEXT")
    private String description;
}


