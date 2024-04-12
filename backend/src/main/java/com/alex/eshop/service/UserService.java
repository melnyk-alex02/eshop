package com.alex.eshop.service;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.userDTOs.UserDTO;
import com.alex.eshop.dto.userDTOs.UserRegisterDTO;
import com.alex.eshop.keycloak.KeycloakService;
import com.alex.eshop.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final KeycloakService keycloakService;
    private final UserMapper userMapper;

    public AccessTokenResponse getAccessToken(String userEmail, String userPassword) {
        return keycloakService.getToken(userEmail, userPassword);
    }

    public AccessTokenResponse refreshAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()){
            throw new IllegalArgumentException("Refresh token is required");
        }
        return keycloakService.refreshToken(refreshToken);
    }

    @PreAuthorize("hasRole('" + Role.USER + "')")
    public void logout() {
        System.out.println("UserService.logout");
        keycloakService.logout();
    }

    public UserDTO getCurrentUser() {
        return  userMapper.toDto(keycloakService.getCurrentUser());
    }

    public UserDTO getUserByUuid(String userUuid) {
        UserRepresentation userRepresentation = keycloakService.getUserByUserUuid(userUuid);

        return userMapper.toDto(userRepresentation);
    }

    public UserDTO createUser(UserRegisterDTO userRegisterDTO) {
        return userMapper.toDto(keycloakService.createUser(userRegisterDTO));
    }

    public void sendEmailVerification() {
        keycloakService.sendEmailVerification();
    }

    public void verifyEmail(String code) {
        keycloakService.verifyEmail(code);
    }

    public void sendEmailForPasswordReset(String email) {
        keycloakService.sendEmailForPasswordReset(email);
    }

    public void verifyPasswordReset(String email, String code, String password, String confirmPassword) {
        keycloakService.verifyPasswordReset(email, code, password, confirmPassword);
    }
}