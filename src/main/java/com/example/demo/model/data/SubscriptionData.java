package com.example.demo.model.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
@Table(name = "subscriptions")
public class SubscriptionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;            // название абонемента

    @Column(nullable = false)
    private Integer durationMonths; // продолжительность в месяцах

    @Column(nullable = false)
    private BigDecimal price;       // цена

    @Column(nullable = true)
    private String description;     // опционально — описание
}
