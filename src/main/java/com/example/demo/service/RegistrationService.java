package com.example.demo.service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.DTO.UserDto;
import com.example.demo.model.Role;
import com.example.demo.model.data.UserData;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserDto register(UserDto dto) {
        UserData user = userMapper.toData(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.CLIENT);
        UserData saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }
}