package com.example.demo.v1controller;

import com.example.demo.model.DTO.AmountDto;
import com.example.demo.model.DTO.SubscriptionDto;
import com.example.demo.model.data.SubscriptionData;
import com.example.demo.model.data.UserData;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.SubscriptionService;
import com.example.demo.service.UserBalanceService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final UserBalanceService userBalanceService;

    public ClientController(UserBalanceService userBalanceService) {
        this.userBalanceService = userBalanceService;
    }

    @GetMapping("/balance")
    public ResponseEntity<Map<String, BigDecimal>> getBalance(Authentication auth) {
        BigDecimal bal = userBalanceService.getBalanceByEmail(auth.getName());
        return ResponseEntity.ok(Map.of("balance", bal));
    }

    @PostMapping("/balance/topup")
    public ResponseEntity<Map<String, BigDecimal>> topUp(
        Authentication auth,
        @RequestBody AmountDto dto) {
        BigDecimal newBal = userBalanceService.addBalance(auth.getName(), dto.getAmount());
        return ResponseEntity.ok(Map.of("balance", newBal));
    }

    @GetMapping("/subscription/current")
    public ResponseEntity<SubscriptionDto> currentSub(Authentication auth) {
        SubscriptionDto sub = userBalanceService.getCurrentSubscription(auth.getName());
        return ResponseEntity.ok(sub);
    }

    @PostMapping("/subscription/purchase/{id}")
    public ResponseEntity<Map<String,Object>> purchase(
        Authentication auth,
        @PathVariable Integer id) {
        Map<String,Object> result = userBalanceService.purchaseSubscription(auth.getName(), id);
        return ResponseEntity.ok(result);
    }
}