package com.example.demo.v1controller;

import com.example.demo.mapper.ScheduleMapper;
import com.example.demo.model.DTO.AmountDto;
import com.example.demo.model.DTO.SchedulesDto;
import com.example.demo.model.DTO.SubscriptionDto;
import com.example.demo.model.data.UserData;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ScheduleClientService;
import com.example.demo.service.UserBalanceService;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final UserBalanceService userBalanceService;
    private final ScheduleClientService scheduleClientService;
    private final ScheduleMapper scheduleMapper;
    private final UserRepository userRepository;

    public ClientController(UserBalanceService userBalanceService,
                            ScheduleClientService scheduleClientService,
                            ScheduleMapper scheduleMapper,
                            UserRepository userRepository) {
        this.userBalanceService = userBalanceService;
        this.scheduleClientService = scheduleClientService;
        this.scheduleMapper = scheduleMapper;
        this.userRepository = userRepository;
    }

    @GetMapping("/balance")
    public ResponseEntity<Map<String, BigDecimal>> getBalance(Authentication auth) {
        BigDecimal bal = userBalanceService.getBalanceByEmail(auth.getName());
        return ResponseEntity.ok(Map.of("balance", bal));
    }

    @PostMapping("/balance/topup")
    public ResponseEntity<Map<String, BigDecimal>> topUp(
        Authentication auth,
        @RequestBody AmountDto dto) {
        BigDecimal newBal = userBalanceService.addBalance(auth.getName(), dto.getAmount());
        return ResponseEntity.ok(Map.of("balance", newBal));
    }

    @GetMapping("/subscription/current")
    public ResponseEntity<SubscriptionDto> currentSub(Authentication auth) {
        SubscriptionDto sub = userBalanceService.getCurrentSubscription(auth.getName());
        return ResponseEntity.ok(sub);
    }

    @PostMapping("/subscription/purchase/{id}")
    public ResponseEntity<Map<String,Object>> purchase(
        Authentication auth,
        @PathVariable Integer id) {
        Map<String,Object> result = userBalanceService.purchaseSubscription(auth.getName(), id);
        return ResponseEntity.ok(result);
    }

    /**
     * Получить список занятий, на которые записан текущий клиент.
     *
     * @param principal Spring Security Principal (имя — email клиента)
     * @return список SchedulesDto
     */
    @GetMapping("/schedules")
    public ResponseEntity<List<SchedulesDto>> getClientSchedules(Principal principal) {
        // 1. Получаем email текущего клиента
        String email = principal.getName();

        // 2. Находим UserData по email (убедимся, что роль — CLIENT)
        UserData client = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Клиент с email=" + email + " не найден"));

        // 3. Берём список SchedulesData через сервис и конвертируем в DTO
        List<SchedulesDto> dtos = scheduleClientService
            .getSchedulesForClient(client.getId())
            .stream()
            .map(scheduleMapper::toDto)
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}