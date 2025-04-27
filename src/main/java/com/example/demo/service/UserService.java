package com.example.demo.service;

import jakarta.transaction.Transactional;
import java.util.List;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.DTO.UserDto;
import com.example.demo.model.Role;
import com.example.demo.model.Data.UserData;
import com.example.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Transactional
    public UserDto getUserById(Integer id) {
        UserData user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Пользователь с id=" + id + " не найден"
            ));
        return userMapper.toDTO(user);
    }

    /**
     * Обновляет пользователя: находит по id, затирает поля из DTO и сохраняет.
     */
    @Transactional
    public UserDto updateUser(Integer id, UserDto dto) {
        // 1) Проверяем, что объект существует
        UserData existing = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Пользователь с id=" + id + " не найден"
            ));

        // 2) Кладём в него новые значения из DTO
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setPhone(dto.getPhone());
        existing.setEmail(dto.getEmail());
        existing.setPosition(dto.getPosition());
        existing.setGender(dto.getGender());
        existing.setRole(dto.getRole());
        existing.setBirthDate(dto.getBirthDate());

        // 3) Сохраняем и возвращаем обновлённый DTO
        UserData saved = userRepository.save(existing);
        return userMapper.toDTO(saved);
    }

    /**
     * Удаляет пользователя по id. Если нет — 404.
     */
    @Transactional
    public void deleteUserById(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Пользователь с id=" + id + " не найден"
            );
        }
        userRepository.deleteById(id);
    }

}

