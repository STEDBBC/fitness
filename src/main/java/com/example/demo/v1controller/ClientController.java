package com.example.demo.v1controller;

import com.example.demo.service.UserBalanceService;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final UserBalanceService balanceService;

    public ClientController(UserBalanceService balanceService) {
        this.balanceService = balanceService;
    }

    /**
     * GET /api/client/balance — возвращает JSON { "balance": 123.45 }
     */
    @GetMapping("/balance")
    public ResponseEntity<Map<String, BigDecimal>> getBalance(Authentication auth) {
        String email = auth.getName();  // Spring Security кладёт email в Authentication
        BigDecimal balance = balanceService.getBalanceByEmail(email);
        return ResponseEntity.ok(Map.of("balance", balance));
    }
}
