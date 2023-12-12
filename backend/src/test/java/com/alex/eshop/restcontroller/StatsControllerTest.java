package com.alex.eshop.restcontroller;

import com.alex.eshop.EshopApplication;
import com.alex.eshop.constants.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EshopApplication.class)
public class StatsControllerTest extends BaseWebTest {

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.ADMIN})
    public void testGetStats() throws Exception {
        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[5].category").value("Smart Home"))
                .andExpect(jsonPath("$.content[5].itemsCount").value(3))
                .andDo(print());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void whenGetStatsWithRoleUser_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/stats")).andExpect(status().isForbidden());
    }
}