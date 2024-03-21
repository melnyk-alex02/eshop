package com.alex.eshop.keycloak;

import com.alex.eshop.webconfig.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeycloakClientFactory {

    private final ApplicationProperties applicationProperties;
    private Keycloak keycloakInstance;

    public synchronized Keycloak getInstance() {
        if (keycloakInstance == null || keycloakInstance.isClosed()) {
            this.keycloakInstance = KeycloakBuilder.builder()
                    .serverUrl(applicationProperties.getKeycloak().getBaseUrl())
                    .realm(applicationProperties.getKeycloak().getRealm())
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .clientId(applicationProperties.getKeycloak().getClientId())
                    .clientSecret(applicationProperties.getKeycloak().getClientSecret()).build();
        }
        return keycloakInstance;
    }
}