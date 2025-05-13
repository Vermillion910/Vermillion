package com.example.vermillion.Model;


import jakarta.persistence.*;
import lombok.*;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                     // обычный суррогатный ключ

    @ManyToOne
    @JoinColumn(name = "developerId", nullable = false)
    private Developer developer;         // связь на разработчика

    @Column(nullable = false, length = 255)
    private String roleName;

}
