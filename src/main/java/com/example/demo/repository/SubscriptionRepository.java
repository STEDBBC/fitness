package com.example.demo.repository;

import com.example.demo.model.data.SubscriptionData;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<SubscriptionData, Integer> {
    List<SubscriptionData> findByPriceBetween(BigDecimal min, BigDecimal max);
}
