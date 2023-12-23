package com.alex.eshop.restcontroller;

import com.alex.eshop.EshopApplication;
import com.alex.eshop.constants.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EshopApplication.class)
@Sql(scripts = "/sqlForControllerTests/cartSql/cart_table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sqlForControllerTests/cartSql/cleanUp_cart.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "/sqlForControllerTests/orderSql/cleanUp_order.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CartControllerTests extends BaseWebTest {

    @Test
    @WithMockUser(value = "userId", authorities = {Role.USER})
    public void testGetAllCarts() throws Exception {
        mockMvc.perform(get("/api/cart/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())

                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].userId").value("userId"))
                .andExpect(jsonPath("$[0].itemId").value(1L))
                .andExpect(jsonPath("$[0].count").value(1))

                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].userId").value("userId"))
                .andExpect(jsonPath("$[1].itemId").value(2L))
                .andExpect(jsonPath("$[1].count").value(1));
    }

    @Test
    @WithMockUser(value = "userId", authorities = {Role.USER})
    public void testAddItemToCart() throws Exception {
        mockMvc.perform(post("/api/cart")
                        .param("itemId", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId").value("userId"))
                .andExpect(jsonPath("$.itemId").value(3L))
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
    @WithMockUser(value = "userId", authorities = {Role.USER})
    public void testUpdateCountOfItem() throws Exception {
        mockMvc.perform(put("/api/cart")
                        .param("itemId", "1")
                        .param("count", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId").value("userId"))
                .andExpect(jsonPath("$.itemId").value(1L))
                .andExpect(jsonPath("$.count").value(5));
    }

    @Test
    @WithMockUser(value = "userId", authorities = {Role.USER})
    public void testDeleteItemFromCart() throws Exception {
        mockMvc.perform(delete("/api/cart/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "userId", authorities = {Role.USER})
    public void testCreateOrderFromCart() throws Exception {
        mockMvc.perform(post("/api/cart/create-order"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.status").value("NEW"))
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.count").exists())
                .andExpect(jsonPath("$.userId").value("userId"))
                .andExpect(jsonPath("$.orderItemDTOList").isArray())
                .andExpect(jsonPath("$.purchasedDate").doesNotExist());
    }
}