package com.example.demo.service;

import com.example.demo.mapper.UserScheduleRegistrationMapper;
import com.example.demo.model.DTO.UserScheduleRegistrationDto;
import com.example.demo.model.data.SchedulesData;
import com.example.demo.model.data.UserScheduleRegistrationData;
import com.example.demo.model.data.UserSubscriptionData;
import com.example.demo.repository.SchedulesRepository;
import com.example.demo.repository.UserScheduleRegistrationRepository;
import com.example.demo.repository.UserSubscriptionRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
public class UserScheduleRegistrationService {
    private final UserScheduleRegistrationRepository regRepo;
    private final UserSubscriptionRepository subsRepo;
    private final SchedulesRepository scheduleRepo;
    private final UserRepository userRepo;
    private final UserScheduleRegistrationMapper mapper;

    public UserScheduleRegistrationService(
        UserScheduleRegistrationRepository regRepo,
        UserSubscriptionRepository subsRepo,
        SchedulesRepository scheduleRepo,
        UserRepository userRepo,
        UserScheduleRegistrationMapper mapper
    ) {
        this.regRepo      = regRepo;
        this.subsRepo     = subsRepo;
        this.scheduleRepo = scheduleRepo;
        this.userRepo     = userRepo;
        this.mapper       = mapper;
    }

    @Transactional(readOnly = true)
    public List<UserScheduleRegistrationDto> getRegistrations(Integer userId) {
        return regRepo.findByUser_Id(userId).stream()
            .map(mapper::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public UserScheduleRegistrationDto register(Integer userId, Integer scheduleId) {
        // проверяем пользователя
        userRepo.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Пользователь не найден"));
        // проверяем занятие
        SchedulesData sch = scheduleRepo.findById(scheduleId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Занятие не найдено"));
        // проверяем, что не дублируем
        if (regRepo.existsByUser_IdAndSchedule_Id(userId, scheduleId)) {
            throw new ResponseStatusException(BAD_REQUEST, "Уже записаны на это занятие");
        }
        // проверяем, что есть активный абонемент на дату занятия
        boolean hasActive = subsRepo.findByUser_IdAndActiveTrue(userId).stream()
            .anyMatch(us -> !sch.getDate().isBefore(us.getStartDate())
                && !sch.getDate().isAfter(us.getEndDate()));
        if (!hasActive) {
            throw new ResponseStatusException(BAD_REQUEST, "Нет активного абонемента на дату занятия");
        }
        // сохраняем регистрацию
        UserScheduleRegistrationData reg = new UserScheduleRegistrationData();
        reg.setUser(userRepo.getReferenceById(userId));
        reg.setSchedule(sch);
        reg.setRegistrationDate(java.time.LocalDateTime.now());
        UserScheduleRegistrationData saved = regRepo.save(reg);
        return mapper.toDTO(saved);
    }
}
