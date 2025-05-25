package com.example.demo.repository;

import com.example.demo.model.data.UserBalanceData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBalanceRepository extends JpaRepository<UserBalanceData, Integer> {
}
