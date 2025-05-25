// com/example/demo/v1controller/UserBalanceController.java
package com.example.demo.v1controller;

import com.example.demo.model.DTO.UserBalanceDto;
import com.example.demo.service.UserBalanceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/user/{userId}/balance")
public class UserBalanceController {
    private final UserBalanceService service;

    public UserBalanceController(UserBalanceService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<UserBalanceDto> getBalance(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.getBalance(userId));
    }

    public static class TopUpRequest {
        @NotNull @DecimalMin(value = "0.01", message = "Сумма должна быть ≥ 0.01")
        public BigDecimal amount;
    }

    @PostMapping("/top-up")
    public ResponseEntity<UserBalanceDto> topUp(
        @PathVariable Integer userId,
        @Valid @RequestBody TopUpRequest req
    ) {
        return ResponseEntity.ok(service.topUp(userId, req.amount));
    }
}
