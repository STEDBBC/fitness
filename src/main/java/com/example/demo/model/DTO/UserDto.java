package com.example.demo.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;
import com.example.demo.model.Role;

@Data
public class UserDto {
    private Integer id;

    @NotBlank(message = "Имя не должно быть пустым")
    private String firstName;

    @NotBlank(message = "Фамилия не должна быть пустой")
    private String lastName;

    @NotNull(message = "Дата рождения обязательна")
    private LocalDate birthDate;

    @Pattern(
        regexp = "\\+?\\d{10,15}",
        message = "Телефон должен быть от 10 до 15 цифр, может начинаться с +"
    )
    private String phone;

    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email обязателен")
    private String email;

    @NotBlank(message = "Пол обязателен")
    @Pattern(regexp = "[MF]", message = "Пол должен быть 'M' или 'F'")
    private String gender;

    private Role role;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank @Size(min = 6, message = "Пароль не менее 6 символов")
    private String password;
}
