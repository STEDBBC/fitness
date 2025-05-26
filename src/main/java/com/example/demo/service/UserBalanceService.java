package com.example.demo.service;

import com.example.demo.mapper.SubscriptionMapper;
import com.example.demo.model.DTO.SubscriptionDto;
import com.example.demo.model.data.SubscriptionData;
import com.example.demo.model.data.UserData;
import com.example.demo.repository.UserRepository;
import java.time.LocalDate;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class UserBalanceService {

    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;

    public UserBalanceService(UserRepository userRepository,
                              SubscriptionService subscriptionService,
                              SubscriptionMapper subscriptionMapper) {
        this.userRepository = userRepository;
        this.subscriptionService = subscriptionService;
        this.subscriptionMapper = subscriptionMapper;
    }

    /**
     * Возвращает баланс пользователя по email.
     */
    public BigDecimal getBalanceByEmail(String email) {
        UserData user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + email));
        return user.getBalance();
    }

    /**
     * Списывает amount с баланса пользователя и сохраняет.
     */
    public void deductBalance(String email, BigDecimal amount) {
        UserData user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + email));
        BigDecimal newBalance = user.getBalance().subtract(amount);
        if (newBalance.signum() < 0) {
            throw new IllegalStateException("Недостаточно средств");
        }
        user.setBalance(newBalance);
        userRepository.save(user);
    }

    /**
     * Пополняет баланс пользователя на сумму amount.
     * @param email — email пользователя
     * @param amount — сумма пополнения (должна быть > 0)
     */
    public BigDecimal addBalance(String email, BigDecimal amount) {
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Сумма пополнения должна быть положительной");
        }
        UserData user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + email));

        BigDecimal newBalance = user.getBalance().add(amount);
        user.setBalance(newBalance);
        userRepository.save(user);
        return newBalance;
    }

    /**
     * Возвращает DTO текущего абонемента пользователя или null, если нет.
     */
    public SubscriptionDto getCurrentSubscription(String email) {
        UserData user = findUser(email);
        SubscriptionData subEntity = user.getSubscription();
        if (subEntity == null) {
            return null;
        }

        SubscriptionDto subscriptionDto = subscriptionMapper.toDTO(subEntity);
        subscriptionDto.setStartDate(user.getSubscriptionStartDate());
        subscriptionDto.setEndDate(user.getSubscriptionEndDate());
        return subscriptionDto;
    }

    /**
     * Логика покупки:
     * 1) Берём цену через service.getSubscriptionById (DTO-шаблон);
     * 2) Списываем баланс;
     * 3) Привязываем к пользователю сущность и ставим даты;
     * 4) Возвращаем новую карту с балансом и полным DTO подписки.
     */
    public Map<String,Object> purchaseSubscription(String email, Integer subscriptionId) {
        // 1) шаблон из настроек
        SubscriptionDto template = subscriptionService.getSubscriptionById(subscriptionId);
        BigDecimal price = template.getPrice();

        // 2) списываем
        deductBalance(email, price);

        // 3) привязываем
        UserData user = findUser(email);
        SubscriptionData entity = subscriptionMapper.toData(subscriptionService.getSubscriptionById(subscriptionId));
        user.setSubscription(entity);

        LocalDate start = LocalDate.now();
        user.setSubscriptionStartDate(start);
        user.setSubscriptionEndDate(start.plusMonths(template.getDurationMonths()));

        userRepository.save(user);

        // 4) готовим DTO с уже заполненными датами
        SubscriptionDto resultDto = subscriptionMapper.toDTO(user.getSubscription());

        return Map.of(
            "balance", user.getBalance(),
            "subscription", resultDto
        );
    }

    // Вспомогательный метод для поиска и исключения, если нет
    private UserData findUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException("Пользователь не найден: " + email));
    }
}