package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.data.ScheduleClientData;
import com.example.demo.model.data.SchedulesData;
import com.example.demo.model.data.UserData;
import com.example.demo.repository.ScheduleClientRepository;
import com.example.demo.repository.SchedulesRepository;
import com.example.demo.repository.SubscriptionRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Сервис для «привязки» клиентов к занятиям и управления ими.
 */
@Service
public class ScheduleClientService {

    private final ScheduleClientRepository scheduleClientRepository;
    private final SchedulesRepository scheduleRepository;
    private final UserRepository userRepository;

    public ScheduleClientService(
        ScheduleClientRepository scheduleClientRepository,
        SchedulesRepository scheduleRepository,
        UserRepository userRepository) {
        this.scheduleClientRepository = scheduleClientRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    /**
     * Добавить клиента на занятие.
     *
     * @param scheduleId ID занятия
     * @param clientId   ID клиента
     */
    @Transactional
    public void addClientToSchedule(Integer scheduleId, Integer clientId) {
        // 1. Проверяем, что занятие существует
        SchedulesData schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Занятие с ID=" + scheduleId + " не найдено"));

        // 2. Проверяем, что пользователь существует
        UserData client = userRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Пользователь с ID=" + clientId + " не найден"));

        // 3. Проверяем, что пользователь — именно клиент
        if (client.getRole() == null || !client.getRole().equals(Role.CLIENT)) {
            throw new RuntimeException("Пользователь с ID=" + clientId + " не является клиентом");
        }

        // 4. Проверяем, что у клиента есть активный абонемент (subscriptionEndDate >= сегодня)
        LocalDate today = LocalDate.now();
        if (client.getSubscriptionEndDate() == null || client.getSubscriptionEndDate().isBefore(today)) {
            throw new RuntimeException("У пользователя с ID=" + clientId + " нет активного абонемента");
        }

        // 5. Проверяем, не записан ли уже на это занятие
        boolean already = scheduleClientRepository.existsByScheduleAndClient(schedule, client);
        if (already) {
            throw new RuntimeException("Клиент уже записан на это занятие");
        }

        // 6. Проверяем вместимость зала
        int capacity = schedule.getHall().getCapacity();
        long currentCount = scheduleClientRepository.countBySchedule(schedule);
        if (currentCount >= capacity) {
            throw new RuntimeException("Вместимость зала " + capacity + " человек уже заполнена");
        }

        // 7. Если всё ок — создаём новую запись и сохраняем
        ScheduleClientData mapping = new ScheduleClientData();
        mapping.setSchedule(schedule);
        mapping.setClient(client);
        scheduleClientRepository.save(mapping);
    }

    /**
     * Удалить клиента с занятия.
     *
     * @param scheduleId ID занятия
     * @param clientId   ID клиента
     */
    @Transactional
    public void removeClientFromSchedule(Integer scheduleId, Integer clientId) {
        // 1. Проверяем, что занятие существует
        SchedulesData schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Занятие с ID=" + scheduleId + " не найдено"));

        // 2. Проверяем, что пользователь существует
        UserData client = userRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Пользователь с ID=" + clientId + " не найден"));

        // 3. Находим запись в schedule_clients
        Optional<ScheduleClientData> opt = scheduleClientRepository.findByScheduleAndClient(schedule, client);

        if (opt.isEmpty()) {
            throw new RuntimeException("Клиент с ID=" + clientId + " не записан на занятие ID=" + scheduleId);
        }

        // 4. Удаляем
        scheduleClientRepository.delete(opt.get());
    }

    /**
     * Получить список всех клиентов, записанных на конкретное занятие.
     *
     * @param scheduleId ID занятия
     * @return список UserData (клиентов)
     */
    @Transactional
    public List<UserData> getClientsForSchedule(Integer scheduleId) {
        // 1. Проверяем, что занятие существует
        SchedulesData schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Занятие с ID=" + scheduleId + " не найдено"));

        // 2. Получаем все записи в schedule_clients для этого занятия
        List<ScheduleClientData> list = scheduleClientRepository.findAllBySchedule(schedule);

        // 3. Из каждой записи достаём только поле client
        return list.stream()
            .map(ScheduleClientData::getClient)
            .collect(Collectors.toList());
    }

    /**
     * Получить список «доступных» клиентов для записи на конкретное занятие:
     *  - роль = CLIENT
     *  - абонемент ещё действует (subscriptionEndDate >= сегодня)
     *  - ещё не записаны на это занятие
     *
     * @param scheduleId ID занятия
     * @return список UserData подходящих клиентов
     */
    @Transactional
    public List<UserData> getEligibleClientsForSchedule(Integer scheduleId) {
        // 1. Убедиться, что занятие существует
        SchedulesData schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Занятие с ID=" + scheduleId + " не найдено"));

        // 2. Сначала берём всех клиентов с активным абонементом
        LocalDate today = LocalDate.now();
        List<UserData> activeClients = userRepository
            .findAllByRoleAndSubscriptionEndDateAfter(Role.CLIENT, today.minusDays(1));
        // (мы передаём today.minusDays(1), чтобы включить тех, у кого subscriptionEndDate == today)

        // 3. Берём всех, кто уже записан на это занятие
        List<UserData> alreadyRegistered = scheduleClientRepository
            .findAllBySchedule(schedule)
            .stream()
            .map(ScheduleClientData::getClient)
            .collect(Collectors.toList());

        // 4. Оставляем только тех, кто НЕ в списке alreadyRegistered
        return activeClients.stream()
            .filter(client -> !alreadyRegistered.contains(client))
            .collect(Collectors.toList());
    }
}