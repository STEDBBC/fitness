// src/main/java/com/example/demo/v1controller/AuthController.java
package com.example.demo.v1controller;

import com.example.demo.model.DTO.UserDto;
import com.example.demo.service.RegistrationService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegistrationService regService;
    private final UserService userService;

    public AuthController(RegistrationService regService,
                          UserService userService) {
        this.regService = regService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserDto dto) {
        regService.register(dto);
        return ResponseEntity.ok().build();
    }

    /** Кто я сейчас? */
    @GetMapping("/me")
    public ResponseEntity<UserDto> whoAmI(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UserDto dto = userService.findByEmail(email)
            .map(u -> { u.setPassword(null); return u; })
            .orElseThrow(() -> new RuntimeException("User not found: " + email));
        return ResponseEntity.ok(dto);
    }
}
