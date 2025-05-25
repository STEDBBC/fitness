package com.example.demo.repository;

import com.example.demo.model.data.UserScheduleRegistrationData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserScheduleRegistrationRepository extends JpaRepository<UserScheduleRegistrationData, Integer> {
    List<UserScheduleRegistrationData> findByUser_Id(Integer userId);
    boolean existsByUser_IdAndSchedule_Id(Integer userId, Integer scheduleId);
}