package com.example.demo.repository;

import com.example.demo.model.data.SchedulesData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulesRepository extends JpaRepository<SchedulesData, Integer> {
}
