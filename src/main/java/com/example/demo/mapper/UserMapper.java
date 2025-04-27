package com.example.demo.mapper;

import com.example.demo.model.DTO.UserDto;
import com.example.demo.model.data.UserData;
import com.example.demo.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserData data);
    User toEntity(UserDto userDto);
    UserData toData(User user);
    UserDto toDTO(User user);

    @Mapping(target = "password", ignore = true)
    UserDto toDTO(UserData entity);

    @Mapping(target = "password", source = "password")
    UserData toData(UserDto dto);
}
