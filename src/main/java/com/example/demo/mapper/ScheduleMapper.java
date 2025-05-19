package com.example.demo.mapper;

import com.example.demo.model.DTO.SchedulesDto;
import com.example.demo.model.data.SchedulesData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mapping(target    = "hallId",      source = "hall.id")
    @Mapping(target    = "hallName",    source = "hall.name")
    @Mapping(target    = "trainerId",   source = "trainer.id")
    @Mapping(
        target     = "trainerName",
        expression = "java(data.getTrainer().getFirstName() + \" \" + data.getTrainer().getLastName())"
    )
    SchedulesDto toDto(SchedulesData data);


    @Mapping(target = "hall",    ignore = true)
    @Mapping(target = "trainer", ignore = true)
    SchedulesData toData(SchedulesDto dto);
}
