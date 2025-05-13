package com.example.vermillion.Model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "ProjectManagerDevelopers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long developerId;

    @Column(nullable = false, length = 255)
    private String firstName;

    @Column(nullable = false, length = 255)
    private String lastName;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 255)
    private String specialization;
}


