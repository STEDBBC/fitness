package com.example.demo.service;

import com.example.demo.mapper.HallMapper;
import com.example.demo.model.DTO.HallsDto;
import com.example.demo.model.data.HallsData;
import com.example.demo.repository.HallsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class HallsService {
    private final HallsRepository hallsRepository;
    private final HallMapper hallMapper;

    public HallsService(HallsRepository hallsRepository, HallMapper hallMapper) {
        this.hallsRepository = hallsRepository;
        this.hallMapper = hallMapper;
    }

    @Transactional
    public HallsDto createHalls(HallsDto dto) {
        HallsData hallsData = hallMapper.toData(dto);
        return hallMapper.toDTO(hallsRepository.save(hallsData));
    }
}
