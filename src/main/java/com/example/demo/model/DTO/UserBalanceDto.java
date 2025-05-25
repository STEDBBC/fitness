package com.example.demo.model.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserBalanceDto {
    @NotNull
    private Integer userId;

    @NotNull
    @Min(value = 0, message = "Баланс не может быть отрицательным")
    private BigDecimal balance;
}
