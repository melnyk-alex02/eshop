package com.alex.eshop.restcontroller;

import com.alex.eshop.EshopApplication;
import com.alex.eshop.constants.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EshopApplication.class)
@Sql(scripts = "/sqlForControllerTests/itemSql/items_table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sqlForControllerTests/itemSql/cleanUp_items.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ItemControllerTests extends BaseWebTest {

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void testGetAllItems() throws Exception {
        mockMvc.perform(get("/api/items")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.content[18].id").value(19L))
                .andExpect(jsonPath("$.content[19].id").value(20L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void testGetLastFiveItems() throws Exception {
        mockMvc.perform(get("/api/items/last")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(21L))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void testGetItemsInCategory() throws Exception {
        mockMvc.perform(get("/api/item?categoryId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.[0].categoryId").value(1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void testGetOneItem() throws Exception {
        mockMvc.perform(get("/api/items/1"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.ADMIN})
    public void testCreateItem() throws Exception {
        String requestBody = "{\"name\": \"item 1\", " +
                "\"description\": \"desc 1\"," +
                " \"categoryId\":\"1\", " +
                "\"imageSrc\":\"img 1\"}";

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))

                .andExpect(jsonPath("$.name").value("item 1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.ADMIN})
    public void testUpdateItem() throws Exception {
        String requestBody = "{\"id\":\"19\"," +
                "\"name\": \"update 1\", " +
                "\"description\": \"desc 1\"," +
                " \"categoryId\":\"1\", " +
                "\"imageSrc\":\"img 1\"}";

        mockMvc.perform(put("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))

                .andExpect(jsonPath("$.name").value("update 1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.ADMIN})
    public void testDeleteItem() throws Exception {
        Long id = 19L;
        mockMvc.perform(delete("/api/items/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.ADMIN})
    public void testUploadItemsFromCsv() throws Exception {
        String content = "name,description,categoryId,imageSrc,price\nitem,desc 1,1,img 1,123";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "items.csv",
                "text/csv",
                content.getBytes()
        );

        mockMvc.perform(multipart("/api/upload-items").file(file))
                .andExpect(jsonPath("$.[0].name").value("item"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void testSearchItems() throws Exception {
        String name = "item";
        Boolean hasImage = true;

        mockMvc.perform(get("/api/items/search?page=0&name=" + name + "&hasImage=" + hasImage))
                .andExpect(jsonPath("$.content.[0].name").value("item 1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void whenCreateItemWithUserRole_thenForbidden() throws Exception {
        String requestBody = "{\"name\": \"item 1\", " +
                "\"description\": \"desc 1\"," +
                " \"categoryId\":\"1\", " +
                "\"imageSrc\":\"img 1\"}";

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))

                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {Role.USER})
    public void whenUpdateItemWithUserRole_thenForbidden() throws Exception {
        String requestBody = "{\"id\":\"19\"," +
                "\"name\": \"update 1\", " +
                "\"description\": \"desc 1\"," +
                " \"categoryId\":\"1\", " +
                "\"imageSrc\":\"img 1\"}";

        mockMvc.perform(put("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))

                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void whenUploadItemsFromCsvWithUserRole_thenForbidden() throws Exception {
        String content = "name,description,categoryId,imageSrc\nitem,desc 1,1,img 1";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "items.csv",
                "text/csv",
                content.getBytes()
        );

        mockMvc.perform(multipart("/api/upload-items").file(file))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void whenDeleteItemWithUserRole_thenForbidden() throws Exception {
        Long id = 20L;
        mockMvc.perform(delete("/api/items/" + id))
                .andExpect(status().isForbidden());
    }
}