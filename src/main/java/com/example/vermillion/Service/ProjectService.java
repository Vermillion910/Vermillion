package com.example.vermillion.Service;

import com.example.vermillion.DTO.ProjectDto;
import com.example.vermillion.Model.Developer;
import com.example.vermillion.Model.Project;
import com.example.vermillion.Repository.DeveloperRepository;
import com.example.vermillion.Repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final DeveloperRepository developerRepository;

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public Project create(ProjectDto dto) {
        var manager = developerRepository.findById(dto.getProjectManagerId())
                .orElseThrow(() -> new EntityNotFoundException("Менеджер не найден"));
        var project = new Project();
        project.setProjectName(dto.getProjectName());
        project.setProjectManager(manager);
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setBudget(dto.getBudget());
        return projectRepository.save(project);
    }

    public Optional<Project> update(Long id, ProjectDto dto) {
        return projectRepository.findById(id).map(existing -> {
            var manager = developerRepository.findById(dto.getProjectManagerId())
                    .orElseThrow(() -> new EntityNotFoundException("Менеджер не найден"));
            existing.setProjectName(dto.getProjectName());
            existing.setProjectManager(manager);
            existing.setStartDate(dto.getStartDate());
            existing.setEndDate(dto.getEndDate());
            existing.setBudget(dto.getBudget());
            return projectRepository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // -----------------------------
    // Новые методы для HTML-контроллера
    // -----------------------------

    /** Возвращает всех разработчиков для выпадающего списка менеджеров */
    public List<Developer> findAllManagers() {
        return developerRepository.findAll();
    }

    /** Сохраняет или обновляет сущность Project напрямую (для HTML-форм) */
    public void saveEntity(Project project) {
        projectRepository.save(project);
    }
}


