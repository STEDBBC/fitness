// com/example/demo/v1controller/UserScheduleRegistrationController.java
package com.example.demo.v1controller;

import com.example.demo.model.DTO.UserScheduleRegistrationDto;
import com.example.demo.service.UserScheduleRegistrationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/{userId}/registrations")
public class UserScheduleRegistrationController {
    private final UserScheduleRegistrationService service;

    public UserScheduleRegistrationController(UserScheduleRegistrationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserScheduleRegistrationDto>> list(
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(service.getRegistrations(userId));
    }

    public static class RegRequest {
        @NotNull
        public Integer scheduleId;
    }

    @PostMapping
    public ResponseEntity<UserScheduleRegistrationDto> register(
        @PathVariable Integer userId,
        @Valid @RequestBody RegRequest req
    ) {
        return ResponseEntity.ok(service.register(userId, req.scheduleId));
    }
}
