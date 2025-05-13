package com.example.vermillion.Service;

import com.example.vermillion.DTO.RoleDto;
import com.example.vermillion.Model.Role;
import com.example.vermillion.Model.Developer;
import com.example.vermillion.Repository.RoleRepository;
import com.example.vermillion.Repository.DeveloperRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final DeveloperRepository developerRepository;

    // CRUD API
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public Role create(RoleDto dto) {
        Developer dev = developerRepository.findById(dto.getDeveloperId())
                .orElseThrow(() -> new EntityNotFoundException("Разработчик не найден"));
        Role role = new Role();
        role.setDeveloper(dev);
        role.setRoleName(dto.getRoleName());
        return roleRepository.save(role);
    }

    public Optional<Role> update(Long id, RoleDto dto) {
        return roleRepository.findById(id).map(existing -> {
            Developer dev = developerRepository.findById(dto.getDeveloperId())
                    .orElseThrow(() -> new EntityNotFoundException("Разработчик не найден"));
            existing.setDeveloper(dev);
            existing.setRoleName(dto.getRoleName());
            return roleRepository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Для Thymeleaf
    public List<Developer> findAllDevelopers() {
        return developerRepository.findAll();
    }

    public void saveEntity(Role role) {
        roleRepository.save(role);
    }
}
