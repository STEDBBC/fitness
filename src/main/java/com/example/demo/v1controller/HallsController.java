package com.example.demo.v1controller;

import com.example.demo.model.DTO.HallsDto;
import com.example.demo.service.HallsService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/halls")
public class HallsController {

    private final HallsService hallsService;

    public HallsController(HallsService hallsService) {
        this.hallsService = hallsService;
    }

    /**
     * POST /api/users Создаёт нового пользователя.
     */
    @PostMapping
    public ResponseEntity<HallsDto> createHall(@Valid @RequestBody HallsDto dto) {
        HallsDto created = hallsService.createHalls(dto);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(location).body(created);
    }

}
