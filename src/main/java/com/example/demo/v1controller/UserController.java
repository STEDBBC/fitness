package com.example.demo.v1controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import com.example.demo.model.DTO.UserDto;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /api/users
     * Создаёт нового пользователя.
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto dto) {
        UserDto created = userService.createUser(dto);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(location).body(created);
    }

    /**
     * GET /api/users/{id}
     * Возвращает пользователя по id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Integer id) {
        UserDto dto = userService.getUserById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * PUT /api/users/{id}
     * Обновляет пользователя.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
        @PathVariable("id") Integer id,
        @Valid @RequestBody UserDto dto
    ) {
        UserDto updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/users/{id}
     * Удаляет пользователя по id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/users
     * Возвращает список всех пользователей.
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> listUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
