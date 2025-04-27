package com.example.demo.model.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HallsDto {
    private Integer id;

    @NotBlank(message = "Имя не должно быть пустым")
    private String name;

    @NotBlank(message = "Вместимость не должна быть пустой")
    private Integer capacity;
}
