package com.example.demo.mapper;

import com.example.demo.model.DTO.UserSubscriptionDto;
import com.example.demo.model.data.UserSubscriptionData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserSubscriptionMapper {
    @Mapping(target = "userId",         source = "user.id")
    @Mapping(target = "subscriptionId", source = "subscription.id")
    UserSubscriptionDto toDTO(UserSubscriptionData data);

    @Mapping(target = "user",         ignore = true)
    @Mapping(target = "subscription", ignore = true)
    UserSubscriptionData toData(UserSubscriptionDto dto);
}
