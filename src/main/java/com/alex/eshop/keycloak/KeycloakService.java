package com.alex.eshop.keycloak;

import com.alex.eshop.webconfig.ApplicationProperties;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class KeycloakService {
    private final ApplicationProperties applicationProperties;

    public KeycloakService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public AccessTokenResponse getToken() {
        Configuration authzConf = new Configuration(this.applicationProperties.getKeycloak().getBaseUrl(),
                this.applicationProperties.getKeycloak().getRealm(), this.applicationProperties.getKeycloak().getClientId(),
                Collections.singletonMap("secret", this.applicationProperties.getKeycloak().getClientSecret()),
                HttpClientBuilder.create().build());
        AuthzClient authzClient = AuthzClient.create(authzConf);
        return authzClient.obtainAccessToken();
    }
}