package com.alex.eshop.dto.userDTOs;

public record UserRegisterDTO(
        String username,
        String email,
        String password,
        String confirmPassword,
        String firstName,
        String lastName
) {

    public UserRegisterDTO withUsername(String username) {
        return new UserRegisterDTO(username, email, password, confirmPassword, firstName, lastName);
    }

    public UserRegisterDTO withEmail(String email) {
        return new UserRegisterDTO(username, email, password, confirmPassword, firstName, lastName);
    }

    public UserRegisterDTO withPassword(String password) {
        return new UserRegisterDTO(username, email, password, confirmPassword, firstName, lastName);
    }

    public UserRegisterDTO withConfirmPassword(String confirmPassword) {
        return new UserRegisterDTO(username, email, password, confirmPassword, firstName, lastName);
    }

    public UserRegisterDTO withFirstName(String firstName) {
        return new UserRegisterDTO(username, email, password, confirmPassword, firstName, lastName);
    }

    public UserRegisterDTO withLastName(String lastName) {
        return new UserRegisterDTO(username, email, password, confirmPassword, firstName, lastName);
    }

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
