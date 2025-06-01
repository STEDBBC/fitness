package com.example.demo.v1controller;

import com.example.demo.mapper.ScheduleMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.DTO.SchedulesDto;
import com.example.demo.model.DTO.UserDto;
import com.example.demo.model.data.SchedulesData;
import com.example.demo.model.data.UserData;
import com.example.demo.repository.SchedulesRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ScheduleClientService;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы тренера с его занятиями и клиентами.
 */
@RestController
@RequestMapping("/api/v1/trainer")
@PreAuthorize("hasRole('TRAINER')")
public class TrainerController {

    private final ScheduleClientService scheduleClientService;
    private final SchedulesRepository schedulesRepository;
    private final UserRepository userRepository;
    private final ScheduleMapper scheduleMapper;
    private final UserMapper userMapper;

    public TrainerController(
        ScheduleClientService scheduleClientService,
        SchedulesRepository schedulesRepository,
        UserRepository userRepository,
        ScheduleMapper scheduleMapper,
        UserMapper userMapper
    ) {
        this.scheduleClientService = scheduleClientService;
        this.schedulesRepository = schedulesRepository;
        this.userRepository = userRepository;
        this.scheduleMapper = scheduleMapper;
        this.userMapper = userMapper;
    }

    /**
     * Получить список всех занятий текущего тренера.
     *
     * @param principal Spring Security Principal (имя — это email тренера)
     * @return список SchedulesDto
     */
    @GetMapping("/schedules")
    public ResponseEntity<List<SchedulesDto>> getTrainerSchedules(Principal principal) {
        // 1. Получаем email текущего тренера из Principal
        String email = principal.getName();

        // 2. Ищем UserData тренера
        UserData trainer = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Тренер с email=" + email + " не найден"));

        // 3. Получаем все занятия и фильтруем по полю trainer
        List<SchedulesDto> dtos = schedulesRepository.findAll().stream()
            .filter(sch -> sch.getTrainer().getId().equals(trainer.getId()))
            .map(scheduleMapper::toDto)
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Получить список клиентов, записанных на конкретное занятие тренера.
     *
     * @param scheduleId ID занятия
     * @param principal  Spring Security Principal (имя — это email тренера)
     * @return список UserDto клиентов
     */
    @GetMapping("/schedules/{scheduleId}/clients")
    public ResponseEntity<List<UserDto>> getClientsForSchedule(
        @PathVariable Integer scheduleId,
        Principal principal
    ) {
        // 1. Убедимся, что занятие действительно принадлежит текущему тренеру
        String email = principal.getName();
        UserData trainer = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Тренер с email=" + email + " не найден"));

        SchedulesData schedule = schedulesRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Занятие с ID=" + scheduleId + " не найдено"));

        if (!schedule.getTrainer().getId().equals(trainer.getId())) {
            throw new RuntimeException("Доступ запрещён: занятие не принадлежит данному тренеру");
        }

        // 2. Берём список клиентов через сервис
        List<UserDto> clients = scheduleClientService.getClientsForSchedule(scheduleId).stream()
            .map(userMapper::toDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(clients);
    }

    /**
     * Добавить клиента на занятие.
     *
     * @param scheduleId ID занятия
     * @param clientId   ID клиента
     * @param principal  Spring Security Principal
     * @return 200 OK или исключение
     */
    @PostMapping("/schedules/{scheduleId}/clients/{clientId}")
    public ResponseEntity<Void> addClientToSchedule(
        @PathVariable Integer scheduleId,
        @PathVariable Integer clientId,
        Principal principal
    ) {
        // 1. Проверяем, что занятие принадлежит текущему тренеру
        String email = principal.getName();
        UserData trainer = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Тренер с email=" + email + " не найден"));

        SchedulesData schedule = schedulesRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Занятие с ID=" + scheduleId + " не найдено"));

        if (!schedule.getTrainer().getId().equals(trainer.getId())) {
            throw new RuntimeException("Доступ запрещён: занятие не принадлежит данному тренеру");
        }

        // 2. Вызываем сервис, который добавит клиента с проверками
        scheduleClientService.addClientToSchedule(scheduleId, clientId);

        return ResponseEntity.ok().build();
    }

    /**
     * Удалить клиента с занятия.
     *
     * @param scheduleId ID занятия
     * @param clientId   ID клиента
     * @param principal  Spring Security Principal
     * @return 200 OK или исключение
     */
    @DeleteMapping("/schedules/{scheduleId}/clients/{clientId}")
    public ResponseEntity<Void> removeClientFromSchedule(
        @PathVariable Integer scheduleId,
        @PathVariable Integer clientId,
        Principal principal
    ) {
        // 1. Проверяем, что занятие принадлежит текущему тренеру
        String email = principal.getName();
        UserData trainer = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Тренер с email=" + email + " не найден"));

        SchedulesData schedule = schedulesRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Занятие с ID=" + scheduleId + " не найдено"));

        if (!schedule.getTrainer().getId().equals(trainer.getId())) {
            throw new RuntimeException("Доступ запрещён: занятие не принадлежит данному тренеру");
        }

        // 2. Вызываем сервис, который удалит клиента
        scheduleClientService.removeClientFromSchedule(scheduleId, clientId);

        return ResponseEntity.ok().build();
    }

    /**
     * Получить список «eligible» клиентов для данной тренировки:
     *  - роль = CLIENT, абонемент активен, ещё не записаны.
     *
     * @param scheduleId ID занятия
     * @param principal  Principal, чтобы проверить, что занятие — у текущего тренера
     * @return список UserDto клиентов, которых можно добавить
     */
    @GetMapping("/schedules/{scheduleId}/eligible-clients")
    public ResponseEntity<List<UserDto>> getEligibleClientsForSchedule(
        @PathVariable Integer scheduleId,
        Principal principal) {

        // 1. Проверяем, что занятие принадлежит текущему тренеру
        String email = principal.getName();
        UserData trainer = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Тренер с email=" + email + " не найден"));

        SchedulesData schedule = schedulesRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Занятие с ID=" + scheduleId + " не найдено"));

        if (!schedule.getTrainer().getId().equals(trainer.getId())) {
            throw new RuntimeException("Доступ запрещён: занятие не принадлежит данному тренеру");
        }

        // 2. Просим сервис отдать нам «eligible» клиентов
        List<UserDto> eligibleDtos = scheduleClientService
            .getEligibleClientsForSchedule(scheduleId)
            .stream()
            .map(userMapper::toDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(eligibleDtos);
    }
}