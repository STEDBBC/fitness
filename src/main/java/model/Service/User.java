package model.Service;

import lombok.Data;
import model.Role;

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
