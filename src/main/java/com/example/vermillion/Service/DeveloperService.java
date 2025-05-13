package com.example.vermillion.Service;

import com.example.vermillion.DTO.DeveloperDto;
import com.example.vermillion.Model.Developer;
import com.example.vermillion.Repository.DeveloperRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeveloperService {

    private final DeveloperRepository developerRepository;

    // CRUD API
    public List<Developer> findAll() {
        return developerRepository.findAll();
    }

    public Optional<Developer> findById(Long id) {
        return developerRepository.findById(id);
    }

    public Developer create(DeveloperDto dto) {
        Developer dev = new Developer();
        dev.setFirstName(dto.getFirstName());
        dev.setLastName(dto.getLastName());
        dev.setEmail(dto.getEmail());
        dev.setPhoneNumber(dto.getPhoneNumber());
        dev.setSpecialization(dto.getSpecialization());
        return developerRepository.save(dev);
    }

    public Optional<Developer> update(Long id, DeveloperDto dto) {
        return developerRepository.findById(id).map(existing -> {
            existing.setFirstName(dto.getFirstName());
            existing.setLastName(dto.getLastName());
            existing.setEmail(dto.getEmail());
            existing.setPhoneNumber(dto.getPhoneNumber());
            existing.setSpecialization(dto.getSpecialization());
            return developerRepository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (developerRepository.existsById(id)) {
            developerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Для Thymeleaf
    public void saveEntity(Developer developer) {
        developerRepository.save(developer);
    }
}
