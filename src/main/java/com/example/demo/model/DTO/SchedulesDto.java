package com.example.demo.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class SchedulesDto {
    private Integer id;

    @NotNull(message = "Дата занятия обязательна")
    private LocalDate date;

    @NotNull(message = "Время начала обязательно")
    private LocalTime startTime;

    @NotNull(message = "Время конца обязательно")
    private LocalTime endTime;

    @NotNull(message = "Зал обязателен")
    private Integer hallId;

    private String hallName;

    @NotNull(message = "Тренер обязателен")
    private Integer trainerId;

    private String trainerName;


    @NotBlank(message = "Тип активности обязателен")
    private String activityType;

    @NotBlank(message = "Статус обязателен")
    private String status;
}