package com.alex.eshop.dto.userDTOs;

public record UserRegisterDTO(
        String username,
        String email,
        String password,
        String confirmPassword,
        String firstName,
        String lastName
) {
    @Override
    public String toString() {
        return "{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
