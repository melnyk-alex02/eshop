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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EshopApplication.class)
@Sql(scripts = "/sqlForControllerTests/categorySql/categories_table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sqlForControllerTests/categorySql/cleanUp_categories.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CategoryControllerTests extends BaseWebTest {

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void testGetAllCategoryList() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON).param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())

                .andExpect(jsonPath("$.content[6].id").exists())
                .andExpect(jsonPath("$.content[6].name").value("category 1"))
                .andExpect(jsonPath("$.content[6].description").value("desc 1"))

                .andExpect(jsonPath("$.content[7].id").value(8L))
                .andExpect(jsonPath("$.content[7].name").value("category 2"))
                .andExpect(jsonPath("$.content[7].description").value("desc 2"))
                .andDo(print());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void testSearchCategories() throws Exception {
        String name = "category 1";

        mockMvc.perform(get("/api/categories/search?name=" + name))
                .andExpect(jsonPath("$.content[0].name").value("category 1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void testGetOneCategory() throws Exception {
        mockMvc.perform(get("/api/categories/7")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id").value(7L))
                .andExpect(jsonPath("$.name").value("category 1"))
                .andExpect(jsonPath("$.description").value("desc 1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.ADMIN})
    public void testCreateCategory() throws Exception {
        String requestBody = "{\"name\": \"category\", \"description\": \"desc 1 \"}";

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))

                .andExpect(jsonPath("$.name").value("category"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.ADMIN})
    public void testUploadCategoriesFromCsv() throws Exception {
        String content = "name,description\ncategory,desc";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "categories.csv",
                "text/csv",
                content.getBytes()
        );

        mockMvc.perform(multipart("/api/upload-categories").file(file))
                .andExpect(jsonPath("$.[0].name").value("category"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.ADMIN})
    public void testUpdateCategory() throws Exception {
        String requestBody = "{\"id\":\"7\",\"name\": \"category\", \"description\": \"desc 1\"}";

        mockMvc.perform(put("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))

                .andExpect(jsonPath("$.name").value("category"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.ADMIN})
    public void testDeleteCategory() throws Exception {
        Long id = 8L;

        mockMvc.perform(delete("/api/categories/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void whenCreateCategoryWithRoleUser_thenForbidden() throws Exception {
        String requestBody = "{\"name\": \"category\", \"description\": \"desc 1 \"}";

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))

                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void whenUploadCategoriesFromCsvWithUserRole_thenForbidden() throws Exception {
        String content = "name,description\ncategory ,desc";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "categories.csv",
                "text/csv",
                content.getBytes()
        );

        mockMvc.perform(multipart("/api/upload-categories").file(file))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(value = "testuser", authorities = {Role.USER})
    public void whenUpdateCategoryWithRoleUser_thenForbidden() throws Exception {
        String requestBody = "{\"id\":\"7\",\"name\": \"category\", \"description\": \"desc 1\"}";

        mockMvc.perform(put("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))

                .andExpect(status().isForbidden());
    }
}