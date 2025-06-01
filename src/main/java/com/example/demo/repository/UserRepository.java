package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;
import com.example.demo.model.Role;
import com.example.demo.model.data.UserData;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserData, Integer> {
    List<UserData> findByRole(Role role);
    Optional<UserData> findByEmail(String email);
    List<UserData> findAllByRoleAndSubscriptionEndDateAfter(Role role, LocalDate date);
}
