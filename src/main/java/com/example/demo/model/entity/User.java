package com.example.demo.model.entity;

import lombok.Data;
import com.example.demo.model.Role;

@Data
public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phone;
    private String position;
    private String gender;
    private String email;
    private Role role;
}
