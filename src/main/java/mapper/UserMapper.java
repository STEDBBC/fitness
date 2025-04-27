package mapper;

import model.DTO.UserDto;
import model.Data.UserData;
import model.Service.User;
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
