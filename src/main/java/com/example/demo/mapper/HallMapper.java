package com.example.demo.mapper;

import com.example.demo.model.DTO.HallsDto;
import com.example.demo.model.data.HallsData;
import com.example.demo.model.entity.Halls;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HallMapper {
    Halls toEntity(HallsData data);
    Halls toEntity(HallsDto dto);
    HallsData toData(Halls halls);
    HallsData toData(HallsDto hallsDto);
    HallsDto toDTO(Halls halls);
    HallsDto toDTO(HallsData hallsData);

}
