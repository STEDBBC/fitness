package com.example.demo.service;

import com.example.demo.mapper.ScheduleMapper;
import com.example.demo.model.DTO.SchedulesDto;
import com.example.demo.model.data.HallsData;
import com.example.demo.model.data.SchedulesData;
import com.example.demo.model.data.UserData;
import com.example.demo.repository.HallsRepository;
import com.example.demo.repository.SchedulesRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ScheduleService {
    private final SchedulesRepository scheduleRepo;
    private final ScheduleMapper scheduleMapper;
    private final UserRepository userRepository;
    private final HallsRepository hallsRepository;

    public ScheduleService(SchedulesRepository scheduleRepo,
                           ScheduleMapper scheduleMapper,
                           UserRepository userRepository,
                           HallsRepository hallsRepository) {
        this.scheduleRepo = scheduleRepo;
        this.scheduleMapper = scheduleMapper;
        this.userRepository = userRepository;
        this.hallsRepository = hallsRepository;
    }

    public List<SchedulesDto> getAllSchedules() {
        return scheduleRepo.findAll().stream()
            .map(scheduleMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public SchedulesDto createSchedule(SchedulesDto dto) {
        SchedulesData entity = scheduleMapper.toData(dto);
        // подгружаем hall и trainer из БД
        HallsData hall = hallsRepository.findById(dto.getHallId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Зал не найден"));
        UserData trainer = userRepository.findById(dto.getTrainerId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Тренер не найден"));

        entity.setHall(hall);
        entity.setStatus(dto.getStatus());
        entity.setTrainer(trainer);
        SchedulesData saved = scheduleRepo.save(entity);
        return scheduleMapper.toDto(saved);
    }

    public SchedulesDto getById(Integer id) {
        SchedulesData data = scheduleRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Расписание с id="+id+" не найдено"));
        return scheduleMapper.toDto(data);
    }

    @Transactional
    public SchedulesDto updateSchedule(Integer id, SchedulesDto dto) {
        SchedulesData existing = scheduleRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Расписание с id=" + id + " не найдено"));

        HallsData hall = hallsRepository.findById(dto.getHallId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Зал с id=" + dto.getHallId() + " не найден"));
        UserData trainer = userRepository.findById(dto.getTrainerId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Тренер с id=" + dto.getTrainerId() + " не найден"));

        existing.setDate(dto.getDate());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        existing.setHall(hall);
        existing.setTrainer(trainer);
        existing.setActivityType(dto.getActivityType());
        existing.setStatus(dto.getStatus());

        return scheduleMapper.toDto(scheduleRepo.save(existing));
    }

    @Transactional
    public void deleteSchedule(Integer id) {
        if (!scheduleRepo.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Расписание с id="+id+" не найдено");
        }
        scheduleRepo.deleteById(id);
    }
}