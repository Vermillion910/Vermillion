package com.example.vermillion.Service;

import com.example.vermillion.DTO.UserDto;
import com.example.vermillion.Model.User;
import com.example.vermillion.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void register(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        // Проверяем, есть ли уже пользователи в системе
        long userCount = userRepository.count();
        
        // Если это первый пользователь, делаем его администратором
        if (userCount == 0) {
            user.setRole(ROLE_ADMIN);
        } else {
            // Иначе устанавливаем роль из DTO или ROLE_USER по умолчанию
            user.setRole(ROLE_USER);
        }
        
        userRepository.save(user);
    }
}