package com.alex.eshop.keycloak;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.UserRegisterDTO;
import com.alex.eshop.exception.ConflictException;
import com.alex.eshop.exception.InvalidDataException;
import com.alex.eshop.webconfig.ApplicationProperties;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public RealmResource realmResource() {
        return keycloakClientFactory.getInstance()
                .realm(applicationProperties.getKeycloak().getRealm());
    }

    public UsersResource usersResource() {
        return keycloakClientFactory.getInstance()
                .realm(applicationProperties.getKeycloak().getRealm())
                .users();
    }

    public List<RoleRepresentation> getRoleRepresentation() {
        List<RoleRepresentation> realmRoles = new ArrayList<>();

        realmRoles.add(realmResource().roles().get(Role.USER).toRepresentation());

        return realmRoles;
    }

    public List<String> getUserRole() {
        return getRoleRepresentation().get(0).getName().lines().toList();
    }

    public UserRepresentation getUserRepresentation(String userUuid) {
        return usersResource()
                .get(userUuid)
                .toRepresentation();
    }

    public UserRepresentation createUser(UserRegisterDTO userRegisterDTO) {
        if (!Objects.equals(userRegisterDTO.getPassword(), userRegisterDTO.getConfirmPassword())) {
            throw new InvalidDataException("Invalid password");
        }

        CredentialRepresentation credentialRepresentation = createPasswordCredentials(userRegisterDTO.getPassword());

        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setUsername(userRegisterDTO.getUserName());
        userRepresentation.setEmail(userRegisterDTO.getEmail());
        userRepresentation.setFirstName(userRegisterDTO.getFirstName());
        userRepresentation.setLastName(userRegisterDTO.getLastName());
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        userRepresentation.setEnabled(true);

        if (usersResource().create(userRepresentation).getStatus() != 201) {
            throw new ConflictException("Problems occurred while creating user");
        }

        String userUuid = usersResource().search(userRegisterDTO.getUserName(), true).get(0).getId();
        userRepresentation.setId(userUuid);

        userRepresentation.setCreatedTimestamp(usersResource().search(userRegisterDTO.getUserName(), true).
                get(0).getCreatedTimestamp());

        usersResource().get(userUuid).roles().realmLevel().add(getRoleRepresentation());

        userRepresentation.setRealmRoles(getUserRole());

        return userRepresentation;
    }
}