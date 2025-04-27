package model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import model.Role;

@Data
public class UserDto {
    private Integer id;

    @NotBlank(message = "Имя не должно быть пустым")
    private String firstName;

    @NotBlank(message = "Фамилия не должна быть пустой")
    private String lastName;

    @Pattern(
        regexp = "\\+?\\d{10,15}",
        message = "Телефон должен быть от 10 до 15 цифр, может начинаться с +"
    )
    private String phone;

    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email обязателен")
    private String email;

    @NotBlank(message = "Позиция обязательна")
    private String position;

    @NotBlank(message = "Пол обязателен")
    @Pattern(regexp = "[MF]", message = "Пол должен быть 'M' или 'F'")
    private String gender;

    @NotNull(message = "Роль обязательна")
    private Role role;
}
