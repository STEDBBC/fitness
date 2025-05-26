package com.example.demo.service;

import com.example.demo.model.data.UserData;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class UserBalanceService {

    private final UserRepository userRepository;

    public UserBalanceService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}