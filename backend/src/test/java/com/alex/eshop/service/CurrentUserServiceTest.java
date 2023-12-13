package com.alex.eshop.service;

import com.alex.eshop.EshopApplication;
import com.alex.eshop.constants.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EshopApplication.class)
class CurrentUserServiceTest {

    @Autowired
    CurrentUserService currentUserService;

    @Test
    void whenNoCurrentUser_thenReturnNull() {
        assertThat(currentUserService.getCurrentUserUuidOptional()).isEmpty();
    }

    @Test
    @WithMockUser
    void whenCurrentUserExist_thenReturnDefaultString() {
        assertThat(currentUserService.getCurrentUserUuidOptional()).isEqualTo(Optional.of("user"));
    }

    @Test
    @WithMockUser(value = "customUserName")
    void whenCustomUserExist_thenReturnCustomUsername() {
        assertThat(currentUserService.getCurrentUserUuidOptional()).isEqualTo(Optional.of("customUserName"));
        assertThat(currentUserService.getCurrentUserAuthorities()).contains(Role.USER); // Spring by default set ROLE_USER
    }

    @Test
    @WithMockUser(value = "customUserName", authorities = {Role.USER, Role.ADMIN})
    void whenUserHasRoles_thenReturnRoles() {
        assertThat(currentUserService.getCurrentUserAuthorities()).containsAll(List.of(Role.USER, Role.ADMIN));
    }

}
