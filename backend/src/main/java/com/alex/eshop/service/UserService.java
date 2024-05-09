package com.alex.eshop.service;

import com.alex.eshop.constants.OneTimePasswordType;
import com.alex.eshop.dto.emailDTOs.EmailDTO;
import com.alex.eshop.dto.userDTOs.UserDTO;
import com.alex.eshop.dto.userDTOs.UserRegisterDTO;
import com.alex.eshop.entity.OneTimePassword;
import com.alex.eshop.exception.InvalidDataException;
import com.alex.eshop.keycloak.KeycloakService;
import com.alex.eshop.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final KeycloakService keycloakService;
    private final UserMapper userMapper;
    private final OneTimePasswordService oneTimePasswordService;
    private final CurrentUserService currentUserService;
    private final EmailService emailService;

    @Value("${frontend.url}")
    private String frontendUrl;

    public AccessTokenResponse getAccessToken(String userEmail, String userPassword) {
        return keycloakService.getToken(userEmail, userPassword);
    }

    public AccessTokenResponse refreshAccessToken(String refreshToken) {
        return keycloakService.refreshToken(refreshToken);
    }

    public void logout() {
        keycloakService.logout(currentUserService.getCurrentUserUuid());
    }

    public UserDTO getCurrentUser() {
        return userMapper.toDto(keycloakService.getCurrentUser(currentUserService.getCurrentUserUuid()));
    }

    public UserDTO getUserByUuid(String userUuid) {
        UserRepresentation userRepresentation = keycloakService.getUserByUserUuid(userUuid);

        return userMapper.toDto(userRepresentation);
    }

    public AccessTokenResponse createUser(UserRegisterDTO userRegisterDTO) {
        return keycloakService.createUser(userRegisterDTO);
    }

    public void sendEmailVerification() {
        String userId = currentUserService.getCurrentUserUuid();

        UserRepresentation userRepresentation = keycloakService.getUserByUserUuid(userId);

        if (userRepresentation.isEmailVerified()) {
            throw new InvalidDataException("Email is already verified");
        }

        OneTimePassword oneTimePassword = oneTimePasswordService.createOneTimePassword(
                userRepresentation.getEmail(),
                OneTimePasswordType.EMAIL_VERIFICATION
        );

        emailService.sendEmail(new EmailDTO(userRepresentation.getEmail(), "Email Verification", "hey!" +
                " you've registered here is your link: " + frontendUrl + "/verify-email/" + oneTimePassword.getValue()));
    }

    public void verifyEmail(String code) {
        String userId = currentUserService.getCurrentUserUuid();

        UserRepresentation userRepresentation = keycloakService.getUserByUserUuid(userId);

        try {
            oneTimePasswordService.validateOneTimePassword(userRepresentation.getEmail(), code, OneTimePasswordType.EMAIL_VERIFICATION);
        } catch (InvalidDataException e) {
            throw new InvalidDataException("Invalid code");
        }

        keycloakService.verifyEmail(userId);
    }

    public void sendEmailForPasswordReset(String email) {
        UserRepresentation userRepresentation = keycloakService.getUserByEmail(email);

        OneTimePassword oneTimePassword = oneTimePasswordService.createOneTimePassword(email, OneTimePasswordType.PASSWORD_RESET);

        emailService.sendEmail(new EmailDTO(userRepresentation.getEmail(), "Password Reset", "hey!" +
                " you've requested password reset here is your link: " + frontendUrl + "/reset-password/" + oneTimePassword.getValue()));
    }

    public void verifyPasswordReset(String email, String code, String password, String confirmPassword) {
        if (!Objects.equals(password, confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        try {
            oneTimePasswordService.validateOneTimePassword(email, code, OneTimePasswordType.PASSWORD_RESET);
        } catch (InvalidDataException e) {
            throw new InvalidDataException("Invalid code");
        }

        keycloakService.updatePassword(email, password);
    }
}