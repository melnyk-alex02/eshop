package com.alex.eshop.keycloak;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.userDTOs.UserRegisterDTO;
import com.alex.eshop.exception.ConflictException;
import com.alex.eshop.exception.DataNotFoundException;
import com.alex.eshop.exception.InvalidDataException;
import com.alex.eshop.webconfig.ApplicationProperties;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.MappingsRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.alex.eshop.keycloak.CredentialsUtils.createPasswordCredentials;

@Service
@RequiredArgsConstructor
public class KeycloakService {
    private static final Logger logger = LoggerFactory.getLogger(KeycloakService.class);
    private final ApplicationProperties applicationProperties;
    private final KeycloakClientFactory keycloakClientFactory;


    public AccessTokenResponse getToken(String email, String password) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "password");
        requestBody.add("client_id", this.applicationProperties.getKeycloak().getClientId());
        requestBody.add("client_secret", this.applicationProperties.getKeycloak().getClientSecret());
        requestBody.add("username", email);
        requestBody.add("password", password);
        requestBody.add("scope", this.applicationProperties.getKeycloak().getScope());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "http://"
                + this.applicationProperties.getKeycloak().getBaseUrl() + "/realms/"
                + this.applicationProperties.getKeycloak().getRealm()
                + "/protocol/openid-connect/token";
        ResponseEntity<AccessTokenResponse> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                entity,
                AccessTokenResponse.class
        );

        return response.getBody();
    }

    public AccessTokenResponse refreshToken(String refreshToken) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("client_id", this.applicationProperties.getKeycloak().getClientId());
        requestBody.add("client_secret", this.applicationProperties.getKeycloak().getClientSecret());
        requestBody.add("scope", this.applicationProperties.getKeycloak().getScope());
        requestBody.add("refresh_token", refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();

        String refreshTokenUrl = "http://"
                + this.applicationProperties.getKeycloak().getBaseUrl() + "/realms/"
                + this.applicationProperties.getKeycloak().getRealm()
                + "/protocol/openid-connect/token";
        ResponseEntity<AccessTokenResponse> response = restTemplate.exchange(
                refreshTokenUrl,
                HttpMethod.POST,
                entity,
                AccessTokenResponse.class);

        return response.getBody();
    }

    public void logout(String userId) {
        usersResource().get(userId).logout();
    }

    public UserRepresentation getCurrentUser(String userId) {
        UserRepresentation userRepresentation = usersResource().get(userId).toRepresentation();
        userRepresentation.setRealmRoles(getUserRoles(userId));

        return userRepresentation;
    }


    private RealmResource realmResource() {
        return keycloakClientFactory.getInstance()
                .realm(this.applicationProperties.getKeycloak().getRealm());
    }

    private UsersResource usersResource() {
        return keycloakClientFactory.getInstance()
                .realm(this.applicationProperties.getKeycloak().getRealm())
                .users();
    }

    public List<String> getUserRoles(String userUuid) {
        try {
            MappingsRepresentation roleRepresentations = usersResource().get(userUuid).roles().getAll();
            return roleRepresentations.getRealmMappings().stream()
                    .map(RoleRepresentation::getName)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private void addRolesToUser(List<RoleRepresentation> realmRoles, String userUuid) {
        usersResource().get(userUuid).roles().realmLevel().add(realmRoles);
    }

    private RoleRepresentation getRole(String role) {
        return realmResource().roles().get(role).toRepresentation();
    }

    public UserRepresentation getUserByUserUuid(String userUuid) {
        UserRepresentation userRepresentation = usersResource().get(userUuid).toRepresentation();
        userRepresentation.setRealmRoles(getUserRoles(userUuid));
        return userRepresentation;
    }

    public void verifyEmail(String userId) {
        UserRepresentation userRepresentation = getUserByUserUuid(userId);

        userRepresentation.setEmailVerified(true);

        usersResource().get(userId).update(userRepresentation);
    }

    public void updatePassword(String email, String password) {
        UserRepresentation user = usersResource().search(email, true).get(0);
        user.setRequiredActions(List.of());
        user.setCredentials(List.of(createPasswordCredentials(password)));
        usersResource().get(user.getId()).update(user);
    }

    public UserRepresentation getUserByEmail(String email) {
        if (usersResource().search(email, true).isEmpty()) {
            throw new DataNotFoundException("User with email " + email + " not found");
        }

        return usersResource().search(email, true).get(0);
    }

    public UserRepresentation createUser(UserRegisterDTO userRegisterDTO) {
        if (!Objects.equals(userRegisterDTO.password(), userRegisterDTO.confirmPassword())) {
            throw new InvalidDataException("Password and Confirm Password does not match");
        }

        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setUsername(userRegisterDTO.email());
        userRepresentation.setEmail(userRegisterDTO.email());
        userRepresentation.setFirstName(userRegisterDTO.firstName());
        userRepresentation.setLastName(userRegisterDTO.lastName());
        userRepresentation.setCredentials(List.of(createPasswordCredentials(userRegisterDTO.password())));
        userRepresentation.setEnabled(true);

        Response response = usersResource().create(userRepresentation);
        HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
        switch (httpStatus) {
            case CREATED -> logger.info("User {} successfully created", userRegisterDTO);
            case CONFLICT -> throw new ConflictException("User " + userRegisterDTO + " already exists");
            case BAD_REQUEST -> throw new InvalidDataException("Cannot create user " + userRegisterDTO + ". Reason: " + parseErrorMessage(response));
            default -> throw new ResponseStatusException(httpStatus, "Problems occurred while creating user " + userRegisterDTO);
        }
        UserRepresentation users = usersResource().search(userRegisterDTO.email(), true).get(0);
        userRepresentation.setId(users.getId());
        userRepresentation.setCreatedTimestamp(users.getCreatedTimestamp());
        addRolesToUser(List.of(getRole(Role.USER)), users.getId());
        userRepresentation.setRealmRoles(getUserRoles(users.getId()));

        return userRepresentation;
    }

    private String parseErrorMessage(Response response) {
        try {
            String responseBody = response.readEntity(String.class);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(responseBody);
            return json.getAsString("errorMessage");
        } catch (ParseException e) {
            return "Unknown";
        }
    }

}