package com.alex.eshop.service;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.userDTOs.UserDTO;
import com.alex.eshop.dto.userDTOs.UserRegisterDTO;
import com.alex.eshop.exception.InvalidDataException;
import com.alex.eshop.keycloak.KeycloakService;
import com.alex.eshop.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private KeycloakService keycloakService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetAccessToken() {
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
        accessTokenResponse.setToken("some token");

        when(keycloakService.getToken()).thenReturn(accessTokenResponse);

        String result = userService.getAccessToken();

        assertEquals(accessTokenResponse.getToken(), result);
    }

    @Test
    public void testGetUserByUuid() {
        String userUuid = "uuid";

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(userUuid);

        UserDTO userDTO = new UserDTO(userUuid,
                null,
                null,
                null,
                null,
                null,
                null);

        when(keycloakService.getUserByUserUuid(any(String.class))).thenReturn(userRepresentation);
        when(userMapper.toDto(any(UserRepresentation.class))).thenReturn(userDTO);

        UserDTO result = userService.getUserByUuid(userUuid);

        verify(keycloakService).getUserByUserUuid(any(String.class));
        verify(userMapper).toDto(any(UserRepresentation.class));

        assertEquals(userDTO.userId(), result.userId());
    }

    @Test
    public void testCreateUser() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("Test",
                "email",
                "superpass",
                "superpass",
                "First Name",
                "Last Name");

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("userUuid");
        userRepresentation.setUsername("Test");
        userRepresentation.setEmail("email");
        userRepresentation.setFirstName("First Name");
        userRepresentation.setLastName("Last Name");
        userRepresentation.setRealmRoles(List.of(Role.USER));

        UserDTO expectedUserDTO = new UserDTO("userUuid",
                "email",
                "Test",
                "First Name",
                "Last Name",
                LocalDateTime.now(),
                List.of(Role.USER));

        when(keycloakService.createUser(any(UserRegisterDTO.class))).thenReturn(userRepresentation);
        when(userMapper.toDto(any(UserRepresentation.class))).thenReturn(expectedUserDTO);

        UserDTO result = userService.createUser(userRegisterDTO);

        verify(keycloakService).createUser(any(UserRegisterDTO.class));
        verify(userMapper).toDto(any(UserRepresentation.class));

        assertEquals(expectedUserDTO.userId(), result.userId());
        assertEquals(expectedUserDTO.username(), result.username());
        assertEquals(expectedUserDTO.email(), result.email());
        assertEquals(expectedUserDTO.firstName(), result.firstName());
        assertEquals(expectedUserDTO.lastName(), result.lastName());
        assertEquals(expectedUserDTO.registerDate(), result.registerDate());
        assertEquals(expectedUserDTO.roles(), result.roles());
    }

    @Test
    public void testCreateUser_WhenPasswordsDoesNotMatch_ShouldThrowException() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("Test",
                "email",
                "superpass",
                "notsuperpass",
                "First Name",
                "Last Name");

        when(keycloakService.createUser(userRegisterDTO)).thenThrow(InvalidDataException.class);

        assertThrows(InvalidDataException.class, () -> userService.createUser(userRegisterDTO));

        verify(keycloakService).createUser(userRegisterDTO);
    }
}
