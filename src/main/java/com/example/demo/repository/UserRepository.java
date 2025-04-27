package com.example.demo.repository;

import java.util.List;
import com.example.demo.model.Role;
import com.example.demo.model.data.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserData, Integer> {
    List<UserData> findByRole(Role role);
}
