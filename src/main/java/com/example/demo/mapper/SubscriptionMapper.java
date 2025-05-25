package com.example.demo.mapper;

import com.example.demo.model.DTO.SubscriptionDto;
import com.example.demo.model.data.SubscriptionData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    /** Преобразует DTO → сущность для сохранения в БД */
    SubscriptionData toData(SubscriptionDto dto);

    /** Преобразует сущность из БД → DTO для выдачи в API */
    SubscriptionDto toDTO(SubscriptionData data);
}
