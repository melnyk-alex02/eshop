package com.alex.eshop.dto.userDTOs;

public record UserRegisterDTO(
        String email,
        String password,
        String confirmPassword,
        String firstName,
        String lastName
) {
    @Override
    public String toString() {
        return "{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
