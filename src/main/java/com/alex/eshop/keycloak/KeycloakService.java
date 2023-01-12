package com.alex.eshop.keycloak;

import com.alex.eshop.dto.UserRegisterDTO;
import com.alex.eshop.exception.Conflict;
import com.alex.eshop.exception.InvalidData;
import com.alex.eshop.webconfig.ApplicationProperties;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static com.alex.eshop.keycloak.CredentialsUtils.createPasswordCredentials;

@Component
public class KeycloakService {
    private final ApplicationProperties applicationProperties;
    private final KeycloakClientFactory keycloakClientFactory;

    public KeycloakService(ApplicationProperties applicationProperties, KeycloakClientFactory keycloakClientFactory) {
        this.applicationProperties = applicationProperties;
        this.keycloakClientFactory = keycloakClientFactory;
    }

    public AccessTokenResponse getToken() {
        Configuration authzConf = new Configuration(this.applicationProperties.getKeycloak().getBaseUrl(),
                this.applicationProperties.getKeycloak().getRealm(), this.applicationProperties.getKeycloak().getClientId(),
                Collections.singletonMap("secret", this.applicationProperties.getKeycloak().getClientSecret()),
                HttpClientBuilder.create().build());
        AuthzClient authzClient = AuthzClient.create(authzConf);
        return authzClient.obtainAccessToken();
    }

    public UserRepresentation getUserRepresentation(String userUuid) {
        UserRepresentation userRepresentation = keycloakClientFactory.getInstance()
                .realm(applicationProperties.getKeycloak().getRealm())
                .users()
                .get(userUuid)
                .toRepresentation();
        return userRepresentation;
    }

    public UserRepresentation createUser(UserRegisterDTO userRegisterDTO) {
        UsersResource usersResource = keycloakClientFactory.getInstance()
                .realm(applicationProperties.getKeycloak().getRealm())
                .users();

        List<RoleRepresentation> realmRoles = new ArrayList<>();

        realmRoles.add(keycloakClientFactory.getInstance()
                .realm(applicationProperties.getKeycloak().getRealm())
                .roles().get("ROLE_USER").toRepresentation());

        if (!Objects.equals(userRegisterDTO.getPassword(), userRegisterDTO.getConfirmPassword())) {
            throw new InvalidData("Invalid password");
        }

        CredentialRepresentation credentialRepresentation = createPasswordCredentials(userRegisterDTO.getPassword());

        UserRepresentation userRepresentation = new UserRepresentation();

        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
        Long createdTimestamp = zonedDateTime.toInstant().toEpochMilli();

        userRepresentation.setUsername(userRegisterDTO.getUserName());
        userRepresentation.setEmail(userRegisterDTO.getEmail());
        userRepresentation.setFirstName(userRegisterDTO.getFirstName());
        userRepresentation.setLastName(userRegisterDTO.getLastName());
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        userRepresentation.setEnabled(true);
        userRepresentation.setCreatedTimestamp(createdTimestamp);

        if (usersResource.create(userRepresentation).getStatus() != 201) {
            throw new Conflict("Problems occurred while creating user");
        }

        String userUuid = keycloakClientFactory.getInstance()
                .realm(applicationProperties.getKeycloak().getRealm())
                .users()
                .search(userRegisterDTO.getUserName()).get(0).getId();
        userRepresentation.setId(userUuid);

        UserResource user = keycloakClientFactory.getInstance()
                .realm(applicationProperties.getKeycloak().getRealm())
                .users()
                .get(userUuid);

        user.roles().realmLevel().add(realmRoles);

        return userRepresentation;
    }
}