package com.example.demo.mapper;

import com.example.demo.model.DTO.UserScheduleRegistrationDto;
import com.example.demo.model.data.UserScheduleRegistrationData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserScheduleRegistrationMapper {
    @Mapping(target = "userId",     source = "user.id")
    @Mapping(target = "scheduleId", source = "schedule.id")
    UserScheduleRegistrationDto toDTO(UserScheduleRegistrationData data);

    @Mapping(target = "user",     ignore = true)
    @Mapping(target = "schedule", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    UserScheduleRegistrationData toData(UserScheduleRegistrationDto dto);
}
