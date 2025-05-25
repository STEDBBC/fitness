package com.example.demo.model.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserSubscriptionDto {
    private Integer id;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer subscriptionId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private Boolean active;
}
