package com.alex.eshop.restcontroller;

import com.alex.eshop.EshopApplication;
import com.alex.eshop.constants.Role;
import com.alex.eshop.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EshopApplication.class)
public class UserControllerTests extends BaseWebTest {

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.ADMIN})
    public void testGetUser() throws Exception {
        String userUuid = "f07f6f24-c0a8-4460-9dc6-c7147583394d"; //userUuid of test-user

        mockMvc.perform(get("/api/users/" + userUuid)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void whenGetUserWithRoleUser_thenForbidden() throws Exception {
        String userUuid = "f07f6f24-c0a8-4460-9dc6-c7147583394d"; //userUuid of test-user

        mockMvc.perform(get("/api/users/" + userUuid)).andExpect(status().isForbidden());
    }
}