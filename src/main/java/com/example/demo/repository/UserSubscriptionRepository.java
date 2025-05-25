package com.example.demo.repository;

import com.example.demo.model.data.UserSubscriptionData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscriptionData, Integer> {
    List<UserSubscriptionData> findByUser_Id(Integer userId);
    List<UserSubscriptionData> findByUser_IdAndActiveTrue(Integer userId);
}
