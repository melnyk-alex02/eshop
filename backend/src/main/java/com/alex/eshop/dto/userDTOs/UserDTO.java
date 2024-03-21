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
}