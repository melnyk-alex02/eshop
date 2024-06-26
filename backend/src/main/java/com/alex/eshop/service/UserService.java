package com.alex.eshop.service;

import com.alex.eshop.dto.userDTOs.UserDTO;
import com.alex.eshop.dto.userDTOs.UserRegisterDTO;
import com.alex.eshop.keycloak.KeycloakService;
import com.alex.eshop.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final KeycloakService keycloakService;
    private final UserMapper userMapper;

    public String getAccessToken() {
        AccessTokenResponse accessTokenResponse = keycloakService.getToken();

        return accessTokenResponse.getToken();
    }

    public UserDTO getUserByUuid(String userUuid) {
        UserRepresentation userRepresentation = keycloakService.getUserByUserUuid(userUuid);

        return userMapper.toDto(userRepresentation);
    }

    public UserDTO createUser(UserRegisterDTO userRegisterDTO) {
        return userMapper.toDto(keycloakService.createUser(userRegisterDTO));
    }
}