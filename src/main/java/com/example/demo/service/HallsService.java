package com.example.demo.service;

import com.example.demo.mapper.HallMapper;
import com.example.demo.model.DTO.HallsDto;
import com.example.demo.model.data.HallsData;
import com.example.demo.repository.HallsRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HallsService {
    private final HallsRepository hallsRepository;
    private final HallMapper hallMapper;

    public HallsService(HallsRepository hallsRepository, HallMapper hallMapper) {
        this.hallsRepository = hallsRepository;
        this.hallMapper = hallMapper;
    }

    @Transactional
    public HallsDto createHall(HallsDto dto) {
        HallsData data = hallMapper.toData(dto);
        return hallMapper.toDTO(hallsRepository.save(data));
    }

    @Transactional(readOnly = true)
    public List<HallsDto> getAllHalls() {
        return hallsRepository.findAll().stream()
            .map(hallMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HallsDto getHallById(Integer id) {
        HallsData hall = hallsRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Зал с id=" + id + " не найден"
            ));
        return hallMapper.toDTO(hall);
    }

    @Transactional
    public HallsDto updateHall(Integer id, HallsDto dto) {
        HallsData existing = hallsRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Зал с id=" + id + " не найден"
            ));
        existing.setName(dto.getName());
        existing.setCapacity(dto.getCapacity());
        return hallMapper.toDTO(hallsRepository.save(existing));
    }

    @Transactional
    public void deleteHallById(Integer id) {
        if (!hallsRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Зал с id=" + id + " не найден"
            );
        }
        hallsRepository.deleteById(id);
    }
}