package com.alex.eshop.restcontroller;

import com.alex.eshop.EshopApplication;
import com.alex.eshop.constants.OrderStatus;
import com.alex.eshop.constants.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EshopApplication.class)
@Sql(scripts = "/sqlForControllerTests/orderSql/order_table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sqlForControllerTests/orderSql/cleanUp_order.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "/sqlForControllerTests/orderSql/orderItem_table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sqlForControllerTests/orderSql/cleanUp_orderItem.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderControllerTests extends BaseWebTest {
    @Test
    @WithMockUser(value = "userId", authorities = {Role.USER})
    public void testGetAllOrdersByUserId() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())

                .andExpect(jsonPath("$.content[0].userId").value("userId"))
                .andExpect(jsonPath("$.content[0].number").value("orderNumber"))
                .andExpect(jsonPath("$.content[0].status").value(OrderStatus.NEW.name()))
                .andExpect(jsonPath("$.content[0].price").value(100))
                .andExpect(jsonPath("$.content[0].orderItemDTOList").isArray());
    }

    @Test
    @WithMockUser(value = "userId", authorities = {Role.USER})
    public void testGetOrderByUserIdAndOrderNumber() throws Exception {
        mockMvc.perform(get("/api/order/orderNumber"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("userId"))
                .andExpect(jsonPath("$.number").value("orderNumber"))
                .andExpect(jsonPath("$.status").value(OrderStatus.NEW.name()))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.orderItemDTOList").isArray());
    }

    @Test
    @WithMockUser(value = "userId", authorities = {Role.USER})
    public void testCancelOrder() throws Exception {
        mockMvc.perform(delete("/api/order/orderNumber"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "userId", authorities = {Role.USER})
    public void testConfirmOrder() throws Exception {
        mockMvc.perform(put("/api/order/confirm/orderNumber"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "userId", authorities = {Role.ADMIN})
    public void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/api/orders/all")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())

                .andExpect(jsonPath("$.content[0].userId").value("userId"))
                .andExpect(jsonPath("$.content[0].number").value("orderNumber"))
                .andExpect(jsonPath("$.content[0].status").value(OrderStatus.NEW.name()))
                .andExpect(jsonPath("$.content[0].price").value(100));
    }

    @Test
    @WithMockUser(value = "userId", authorities = {Role.USER})
    public void whenGetAllOrdersWithRoleUser_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/orders/all")).andExpect(status().isForbidden());
    }
}