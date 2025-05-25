package com.example.demo.model.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class SubscriptionDto {
    private Integer id;

    @NotBlank(message = "Название обязательно")
    private String name;

    @NotNull(message = "Длительность обязательна")
    @Min(value = 1, message = "Длительность ≥ 1 месяц")
    private Integer durationMonths;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть положительной")
    private BigDecimal price;

    private String description;
}
