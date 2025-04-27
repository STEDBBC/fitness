package com.example.demo.model.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HallsDto {
    private Integer id;

    @NotBlank(message = "Имя не должно быть пустым")
    private String name;

    @NotNull(message = "Вместимость обязательна")
    @Min(value = 1, message = "Вместимость должна быть не менее 1")
    private Integer capacity;
}
