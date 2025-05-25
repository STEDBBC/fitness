package com.example.demo.v1controller;

import com.example.demo.model.DTO.SubscriptionDto;
import com.example.demo.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /** GET /api/subscriptions — список всех */
    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> listAll() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    /** GET /api/subscriptions/{id} — получить по id */
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionById(id));
    }

    /** POST /api/subscriptions — создать */
    @PostMapping
    public ResponseEntity<SubscriptionDto> create(
        @Valid @RequestBody SubscriptionDto dto
    ) {
        SubscriptionDto created = subscriptionService.createSubscription(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(uri).body(created);
    }

    /** PUT /api/subscriptions/{id} — обновить */
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDto> update(
        @PathVariable Integer id,
        @Valid @RequestBody SubscriptionDto dto
    ) {
        return ResponseEntity.ok(subscriptionService.updateSubscription(id, dto));
    }

    /** DELETE /api/subscriptions/{id} — удалить */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}
