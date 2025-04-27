package com.example.demo.service;

import jakarta.transaction.Transactional;
import java.util.List;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.DTO.UserDto;
import com.example.demo.model.Role;
import com.example.demo.model.Data.UserData;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserData> getTrainers() {
        return userRepository.findByRole(Role.TRAINER);
    }


    @Transactional
    public UserDto createUser(UserDto dto) {
        UserData userData = userMapper.toData(dto);
        return userMapper.toDTO(userRepository.save(userData));
    }

}

