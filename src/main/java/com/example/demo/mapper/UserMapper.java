package com.example.demo.mapper;

import com.example.demo.model.DTO.UserDto;
import com.example.demo.model.data.UserData;
import com.example.demo.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserData data);
    User toEntity(UserDto userDto);
    UserData toData(User user);
    UserData toData(UserDto userDto);
    UserDto toDTO(User user);
    UserDto toDTO(UserData userData);
}
