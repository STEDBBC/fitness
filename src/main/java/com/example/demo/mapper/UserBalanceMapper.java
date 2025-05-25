package com.example.demo.mapper;

import com.example.demo.model.DTO.UserBalanceDto;
import com.example.demo.model.data.UserBalanceData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserBalanceMapper {
    UserBalanceData toData(UserBalanceDto dto);
    UserBalanceDto toDTO(UserBalanceData data);
}
