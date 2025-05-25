package com.example.demo.model.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserScheduleRegistrationDto {
    private Integer id;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer scheduleId;

    private LocalDateTime registrationDate;
}
