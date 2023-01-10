package com.alex.eshop.service;

import com.alex.eshop.dto.UserDTO;
import com.alex.eshop.dto.UserRegisterDTO;
import com.alex.eshop.exception.InvalidData;
import com.alex.eshop.keycloak.KeycloakClientFactory;
import com.alex.eshop.keycloak.KeycloakService;
import com.alex.eshop.mapper.UserMapper;
import com.alex.eshop.webconfig.ApplicationProperties;
import jakarta.transaction.Transactional;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.alex.eshop.keycloak.Credentials.createPasswordCredentials;

@Service
@Transactional
public class UserService {

    private final KeycloakService keycloakService;
    private final KeycloakClientFactory keycloakClientFactory;
    private final ApplicationProperties applicationProperties;
    private final UserMapper userMapper;

    public UserService(KeycloakService keycloakService, KeycloakClientFactory keycloakClientFactory,
                       ApplicationProperties applicationProperties, UserMapper userMapper) {
        this.keycloakService = keycloakService;
        this.keycloakClientFactory = keycloakClientFactory;
        this.applicationProperties = applicationProperties;
        this.userMapper = userMapper;
    }

    public String getAccessToken() {
        AccessTokenResponse accessTokenResponse = keycloakService.getToken();
        return accessTokenResponse.getToken();
    }

    public UserRepresentation getUserRepresentation(String userUuid) {
        UserRepresentation userRepresentation = keycloakClientFactory.getInstance()
                .realm(applicationProperties.getKeycloak().getRealm())
                .users()
                .get(userUuid)
                .toRepresentation();
        return userRepresentation;
    }

    public UserDTO getUser(String userUuid) {
        UserRepresentation userRepresentation = getUserRepresentation(userUuid);

        return userMapper.toDto(userRepresentation);
    }

    public UserDTO createUser(UserRegisterDTO userRegisterDTO) {
        UsersResource usersResource = keycloakClientFactory.getInstance()
                .realm(applicationProperties.getKeycloak().getRealm())
                .users();

        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            throw new InvalidData("Invalid password");
        }

        CredentialRepresentation credentialRepresentation = createPasswordCredentials(userRegisterDTO.getPassword());

        UserRepresentation userRepresentation = new UserRepresentation();

        ArrayList<String> realmRoles = new ArrayList<>();
        realmRoles.add("ROLE_USER");

        userRepresentation.setUsername(userRegisterDTO.getUserName());
        userRepresentation.setEmail(userRegisterDTO.getEmail());
        userRepresentation.setFirstName(userRegisterDTO.getFirstName());
        userRepresentation.setLastName(userRegisterDTO.getLastName());
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        userRepresentation.setEnabled(true);
        userRepresentation.setRealmRoles(realmRoles);

        usersResource.create(userRepresentation);

        return userMapper.toDto(userRepresentation);
    }
}