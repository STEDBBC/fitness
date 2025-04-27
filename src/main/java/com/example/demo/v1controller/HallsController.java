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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
public class HallsController {

    private final HallsService hallsService;

    public HallsController(HallsService hallsService) {
        this.hallsService = hallsService;
    }

    /** Создать новый зал */
    @PostMapping
    public ResponseEntity<HallsDto> createHall(@Valid @RequestBody HallsDto dto) {
        HallsDto created = hallsService.createHall(dto);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(location).body(created);
    }

    /** Получить список всех залов */
    @GetMapping
    public ResponseEntity<List<HallsDto>> listHalls() {
        return ResponseEntity.ok(hallsService.getAllHalls());
    }

    /** Получить зал по id */
    @GetMapping("/{id}")
    public ResponseEntity<HallsDto> getHall(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(hallsService.getHallById(id));
    }

    /** Обновить зал по id */
    @PutMapping("/{id}")
    public ResponseEntity<HallsDto> updateHall(
        @PathVariable("id") Integer id,
        @Valid @RequestBody HallsDto dto
    ) {
        return ResponseEntity.ok(hallsService.updateHall(id, dto));
    }

    /** Удалить зал по id */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHall(@PathVariable("id") Integer id) {
        hallsService.deleteHallById(id);
        return ResponseEntity.noContent().build();
    }
}
