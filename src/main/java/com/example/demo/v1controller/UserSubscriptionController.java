package com.example.demo.v1controller;

import com.example.demo.model.DTO.UserSubscriptionDto;
import com.example.demo.service.UserSubscriptionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/{userId}/subscriptions")
public class UserSubscriptionController {
    private final UserSubscriptionService service;

    public UserSubscriptionController(UserSubscriptionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserSubscriptionDto>> list(
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(service.getUserSubscriptions(userId));
    }

    public static class PurchaseRequest {
        @NotNull
        public Integer subscriptionId;
    }

    @PostMapping("/purchase")
    public ResponseEntity<UserSubscriptionDto> purchase(
        @PathVariable Integer userId,
        @Valid @RequestBody PurchaseRequest req
    ) {
        return ResponseEntity.ok(service.purchase(userId, req.subscriptionId));
    }
}
