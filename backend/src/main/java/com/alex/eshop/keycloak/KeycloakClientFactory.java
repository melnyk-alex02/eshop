package com.alex.eshop.keycloak;

import com.alex.eshop.webconfig.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;

@Component
@RequiredArgsConstructor
public class KeycloakClientFactory {

    private final ApplicationProperties applicationProperties;
    private Keycloak keycloakInstance;
    private final Logger logger = LoggerFactory.getLogger(KeycloakClientFactory.class.getName());

    public synchronized Keycloak getInstance() {
        logger.info("Retrieving Keycloak instance for realm: {}", applicationProperties.getKeycloak().getRealm());
        if (keycloakInstance == null || keycloakInstance.isClosed()) {
            this.keycloakInstance = KeycloakBuilder.builder()
                    .serverUrl("http://" + applicationProperties.getKeycloak().getBaseUrl())
                    .realm(applicationProperties.getKeycloak().getRealm())
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .clientId(applicationProperties.getKeycloak().getClientId())
                    .clientSecret(applicationProperties.getKeycloak().getClientSecret()).build();
        }
        return keycloakInstance;
    }
}