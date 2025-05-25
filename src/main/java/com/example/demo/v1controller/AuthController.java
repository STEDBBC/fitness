package com.example.demo.v1controller;

import com.example.demo.model.DTO.UserDto;
import com.example.demo.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegistrationService regService;


    public AuthController(RegistrationService regService) {
        this.regService = regService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserDto dto) {
        regService.register(dto);
        return ResponseEntity.ok().build();
    }
}