package com.example.demo.v1controller;

import com.example.demo.model.DTO.SchedulesDto;
import com.example.demo.service.ScheduleService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService service;

    public ScheduleController(ScheduleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SchedulesDto>> listAll() {
        return ResponseEntity.ok(service.getAllSchedules());
    }

    @PostMapping
    public ResponseEntity<SchedulesDto> create(@Valid @RequestBody SchedulesDto dto) {
        SchedulesDto created = service.createSchedule(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchedulesDto> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchedulesDto> update(
        @PathVariable("id") Integer id,
        @Valid @RequestBody SchedulesDto dto
    ) {
        return ResponseEntity.ok(service.updateSchedule(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable("id") Integer id) {
        service.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
