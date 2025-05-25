package com.example.demo.service;

import com.example.demo.mapper.UserBalanceMapper;
import com.example.demo.model.DTO.UserBalanceDto;
import com.example.demo.model.data.UserBalanceData;
import com.example.demo.repository.UserBalanceRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;


import static org.springframework.http.HttpStatus.*;

@Service
public class UserBalanceService {
    private final UserBalanceRepository balanceRepo;
    private final UserRepository userRepo;
    private final UserBalanceMapper mapper;

    public UserBalanceService(UserBalanceRepository balanceRepo,
                              UserRepository userRepo,
                              UserBalanceMapper mapper) {
        this.balanceRepo = balanceRepo;
        this.userRepo    = userRepo;
        this.mapper      = mapper;
    }

    @Transactional(readOnly = true)
    public UserBalanceDto getBalance(Integer userId) {
        // если нет записи — возвращаем 0
        BigDecimal bal = balanceRepo.findById(userId)
            .map(UserBalanceData::getBalance)
            .orElse(BigDecimal.ZERO);
        UserBalanceDto dto = new UserBalanceDto();
        dto.setUserId(userId);
        dto.setBalance(bal);
        return dto;
    }

    @Transactional
    public UserBalanceDto topUp(Integer userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "Сумма должна быть положительной");
        }
        // проверяем, что пользователь существует
        userRepo.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Пользователь не найден"));

        UserBalanceData data = balanceRepo.findById(userId)
            .orElseGet(() -> {
                UserBalanceData d = new UserBalanceData();
                d.setUserId(userId);
                d.setBalance(BigDecimal.ZERO);
                return d;
            });
        data.setBalance(data.getBalance().add(amount));
        UserBalanceData saved = balanceRepo.save(data);
        return mapper.toDTO(saved);
    }
}