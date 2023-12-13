package com.alex.eshop.service;

import com.alex.eshop.EshopApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

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
    @WithMockUser("customUserName")
    void whenCustomUserExist_thenReturnCustomUsername() {
        assertThat(currentUserService.getCurrentUserUuidOptional()).isEqualTo(Optional.of("customUserName"));
    }

}
