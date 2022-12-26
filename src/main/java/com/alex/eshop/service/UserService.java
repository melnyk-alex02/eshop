package com.alex.eshop.service;

import com.alex.eshop.dto.UserDTO;
import com.alex.eshop.dto.UserRegisterDTO;
import com.alex.eshop.exception.InvalidData;
import com.alex.eshop.keycloak.KeycloakClientFactory;
import com.alex.eshop.keycloak.KeycloakService;
import com.alex.eshop.webconfig.ApplicationProperties;
import jakarta.transaction.Transactional;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;

import static com.alex.eshop.keycloak.Credentials.createPasswordCredentials;

@Service
@Transactional
public class UserService {

    private final KeycloakService keycloakService;
    private final KeycloakClientFactory keycloakClientFactory;
    private final ApplicationProperties applicationProperties;

    public UserService(KeycloakService keycloakService, KeycloakClientFactory keycloakClientFactory, ApplicationProperties applicationProperties) {
        this.keycloakService = keycloakService;
        this.keycloakClientFactory = keycloakClientFactory;
        this.applicationProperties = applicationProperties;
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
        UserRepresentation userRepresentation = keycloakClientFactory.getInstance()
                .realm(applicationProperties.getKeycloak().getRealm())
                .users().get(userUuid).toRepresentation();

        UserDTO userDTO = new UserDTO();

        userDTO.setUserUuid(userRepresentation.getId());
        userDTO.setFirstName(userRepresentation.getFirstName());
        userDTO.setLastName(userRepresentation.getLastName());
        userDTO.setEmail(userRepresentation.getEmail());
        userDTO.setRoles(userRepresentation.getRealmRoles());

        return userDTO;
    }

    public Response createUser(UserRegisterDTO userRegisterDTO) {
        UsersResource usersResource = keycloakClientFactory.getInstance().realm(applicationProperties.getKeycloak().getRealm()).users();

        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            throw new InvalidData("Invalid password");
        }

        CredentialRepresentation credentials = createPasswordCredentials(userRegisterDTO.getPassword());

        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setUsername(userRegisterDTO.getUserName());
        userRepresentation.setCredentials(Collections.singletonList(credentials));
        userRepresentation.setFirstName(userRegisterDTO.getFirstName());
        userRepresentation.setLastName(userRegisterDTO.getLastName());
        userRepresentation.setEmail(userRegisterDTO.getEmail());
        userRepresentation.setEnabled(true);

        return usersResource.create(userRepresentation);
    }
}