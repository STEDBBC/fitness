package com.example.demo.model.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "subscriptions_users_balance")
public class UserBalanceData {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;
}
