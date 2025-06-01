package com.example.demo.repository;

import com.example.demo.model.data.ScheduleClientData;
import com.example.demo.model.data.SchedulesData;
import com.example.demo.model.data.UserData;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleClientRepository extends JpaRepository<ScheduleClientData, Integer> {
    // Найти всех клиентов, записанных на конкретное занятие
    List<ScheduleClientData> findAllBySchedule(SchedulesData schedule);

    // Найти запись конкретного клиента на конкретное занятие (чтобы предотвратить дубли)
    boolean existsByScheduleAndClient(SchedulesData schedule, UserData client);

    // Подсчитать, сколько клиентов уже записано на занятие
    long countBySchedule(SchedulesData schedule);

    // Добавляем этот метод:
    // Поиск конкретной «строки» (ScheduleClientData) по занятию и клиенту.
    Optional<ScheduleClientData> findByScheduleAndClient(SchedulesData schedule, UserData client);
}