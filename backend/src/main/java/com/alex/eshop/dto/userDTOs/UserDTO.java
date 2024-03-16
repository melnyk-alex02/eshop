package com.alex.eshop.dto.userDTOs;

import java.time.LocalDateTime;
import java.util.List;

public record UserDTO(
        String userId,
        String email,
        String username,
        String firstName,
        String lastName,
        LocalDateTime registerDate,
        List<String> roles
) {
    public UserDTO withUserId(String userId) {
        return new UserDTO(userId, email, username, firstName, lastName, registerDate, roles);
    }

    public UserDTO withEmail(String email) {
        return new UserDTO(userId, email, username, firstName, lastName, registerDate, roles);
    }

    public UserDTO withUsername(String username) {
        return new UserDTO(userId, email, username, firstName, lastName, registerDate, roles);
    }

    public UserDTO withFirstName(String firstName) {
        return new UserDTO(userId, email, username, firstName, lastName, registerDate, roles);
    }

    public UserDTO withLastName(String lastName) {
        return new UserDTO(userId, email, username, firstName, lastName, registerDate, roles);
    }

    public UserDTO withRegisterDate(LocalDateTime registerDate) {
        return new UserDTO(userId, email, username, firstName, lastName, registerDate, roles);
    }

    public UserDTO withRoles(List<String> roles) {
        return new UserDTO(userId, email, username, firstName, lastName, registerDate, roles);
    }
}