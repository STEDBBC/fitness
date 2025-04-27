package service;

import jakarta.transaction.Transactional;
import java.util.List;
import mapper.UserMapper;
import model.DTO.UserDto;
import model.Role;
import model.Data.UserData;
import matrix.repository.UserRepository;
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

