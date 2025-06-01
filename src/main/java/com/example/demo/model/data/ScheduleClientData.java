package com.example.demo.model.data;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(
    name = "schedule_clients",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_schedule_client",
        columnNames = {"schedule_id", "client_id"}
    )
)
public class ScheduleClientData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // явная связь с SchedulesData
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private SchedulesData schedule;

    // явная связь с UserData (клиентом)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private UserData client;
}