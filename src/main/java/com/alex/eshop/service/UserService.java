package com.alex.eshop.service;

import com.alex.eshop.keycloak.KeycloakClientFactory;
import com.alex.eshop.keycloak.KeycloakService;
import com.alex.eshop.webconfig.ApplicationProperties;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
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

}