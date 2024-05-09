package com.alex.eshop.webconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Keycloak keycloak = new Keycloak();

    public Keycloak getKeycloak() {
        return keycloak;
    }

    @Getter
    @Setter
    public static class Keycloak {
        private String realm;
        private String baseUrl;
        private String clientId;
        private String clientSecret;
        private String scope;
    }
}