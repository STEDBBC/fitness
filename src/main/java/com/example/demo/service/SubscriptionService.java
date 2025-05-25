package com.example.demo.service;

import com.example.demo.mapper.SubscriptionMapper;
import com.example.demo.model.DTO.SubscriptionDto;
import com.example.demo.model.data.SubscriptionData;
import com.example.demo.repository.SubscriptionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepo;
    private final SubscriptionMapper subscriptionMapper;

    public SubscriptionService(
        SubscriptionRepository subscriptionRepo,
        SubscriptionMapper subscriptionMapper
    ) {
        this.subscriptionRepo = subscriptionRepo;
        this.subscriptionMapper = subscriptionMapper;
    }

    /** Создать новый абонемент */
    @Transactional
    public SubscriptionDto createSubscription(SubscriptionDto dto) {
        SubscriptionData data = subscriptionMapper.toData(dto);
        SubscriptionData saved = subscriptionRepo.save(data);
        return subscriptionMapper.toDTO(saved);
    }

    /** Список всех абонементов */
    @Transactional(readOnly = true)
    public List<SubscriptionDto> getAllSubscriptions() {
        return subscriptionRepo.findAll().stream()
            .map(subscriptionMapper::toDTO)
            .collect(Collectors.toList());
    }

    /** Получить абонемент по id */
    @Transactional(readOnly = true)
    public SubscriptionDto getSubscriptionById(Integer id) {
        SubscriptionData data = subscriptionRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Абонемент с id=" + id + " не найден"
            ));
        return subscriptionMapper.toDTO(data);
    }

    /** Обновить абонемент */
    @Transactional
    public SubscriptionDto updateSubscription(Integer id, SubscriptionDto dto) {
        SubscriptionData existing = subscriptionRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Абонемент с id=" + id + " не найден"
            ));
        // Обновляем поля
        existing.setName(dto.getName());
        existing.setDurationMonths(dto.getDurationMonths());
        existing.setPrice(dto.getPrice());
        existing.setDescription(dto.getDescription());
        SubscriptionData saved = subscriptionRepo.save(existing);
        return subscriptionMapper.toDTO(saved);
    }

    /** Удалить абонемент */
    @Transactional
    public void deleteSubscription(Integer id) {
        if (!subscriptionRepo.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Абонемент с id=" + id + " не найден"
            );
        }
        subscriptionRepo.deleteById(id);
    }
}