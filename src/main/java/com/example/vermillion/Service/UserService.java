package com.example.vermillion.Service;

import com.example.vermillion.DTO.UserDto;
import com.example.vermillion.Model.User;
import com.example.vermillion.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void register(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        if (userRepository.count() == 0) {
            user.setRole("ROLE_ADMIN");
        } else {
            user.setRole(userDto.getRole());
        }
        
        userRepository.save(user);
    }

    @Transactional
    public void updateUsername(String currentUsername, String newUsername) {
        userRepository.findByUsername(currentUsername)
                .ifPresent(user -> {
                    user.setUsername(newUsername);
                    userRepository.save(user);
                });
    }

    @Transactional
    public void updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        
        if (!user.getUsername().equals(userDto.getUsername()) && userExists(userDto.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        
        user.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setRole(userDto.getRole());
        
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Пользователь не найден");
        }
        userRepository.deleteById(id);
    }
}