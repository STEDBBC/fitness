package com.example.demo.repository;

import com.example.demo.model.data.HallsData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallsRepository extends JpaRepository<HallsData, Integer> {

}
