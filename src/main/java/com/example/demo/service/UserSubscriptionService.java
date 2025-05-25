package com.example.demo.service;

import com.example.demo.mapper.UserSubscriptionMapper;
import com.example.demo.model.DTO.UserSubscriptionDto;
import com.example.demo.model.data.SubscriptionData;
import com.example.demo.model.data.UserBalanceData;
import com.example.demo.model.data.UserSubscriptionData;
import com.example.demo.model.data.UserData;
import com.example.demo.repository.SubscriptionRepository;
import com.example.demo.repository.UserBalanceRepository;
import com.example.demo.repository.UserSubscriptionRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
public class UserSubscriptionService {
    private final UserSubscriptionRepository subsRepo;
    private final UserBalanceRepository balanceRepo;
    private final SubscriptionRepository subscriptionRepo;
    private final UserRepository userRepo;
    private final UserSubscriptionMapper mapper;

    public UserSubscriptionService(UserSubscriptionRepository subsRepo,
                                   UserBalanceRepository balanceRepo,
                                   SubscriptionRepository subscriptionRepo,
                                   UserRepository userRepo,
                                   UserSubscriptionMapper mapper) {
        this.subsRepo           = subsRepo;
        this.balanceRepo        = balanceRepo;
        this.subscriptionRepo   = subscriptionRepo;
        this.userRepo           = userRepo;
        this.mapper             = mapper;
    }

    @Transactional(readOnly = true)
    public List<UserSubscriptionDto> getUserSubscriptions(Integer userId) {
        return subsRepo.findByUser_Id(userId).stream()
            .map(mapper::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public UserSubscriptionDto purchase(Integer userId, Integer subscriptionId) {
        UserData user = userRepo.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Пользователь не найден"));
        SubscriptionData plan = subscriptionRepo.findById(subscriptionId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "План не найден"));
        UserBalanceData bal = balanceRepo.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Сначала пополните баланс"));

        if (bal.getBalance().compareTo(plan.getPrice()) < 0) {
            throw new ResponseStatusException(BAD_REQUEST, "Недостаточно средств");
        }
        // списываем цену
        bal.setBalance(bal.getBalance().subtract(plan.getPrice()));
        balanceRepo.save(bal);

        // создаём покупку
        LocalDate start = LocalDate.now();
        LocalDate end   = start.plusMonths(plan.getDurationMonths());
        UserSubscriptionData us = new UserSubscriptionData();
        us.setUser(user);
        us.setSubscription(plan);
        us.setStartDate(start);
        us.setEndDate(end);
        us.setActive(true);
        UserSubscriptionData saved = subsRepo.save(us);
        return mapper.toDTO(saved);
    }
}
