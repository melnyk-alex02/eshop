package controllerTests;

import com.alex.eshop.EshopApplication;
import config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(classes = EshopApplication.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class CategoryControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @SqlGroup({@Sql(scripts = "/sqlForControllerTests/categorySql/categories_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sqlForControllerTests/categorySql/cleanUp_categories.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void testGetAllCategoryList() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
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
    @SqlGroup({@Sql(scripts = "/sqlForControllerTests/categorySql/categories_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sqlForControllerTests/categorySql/cleanUp_categories.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    public void testGetOneCategory() throws Exception {
        mockMvc.perform(get("/api/categories/7")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(7L))
                .andExpect(jsonPath("$.name").value("category 1"))
                .andExpect(jsonPath("$.description").value("desc 1"));
    }

    @Test
    @Sql(scripts = "/sqlForControllerTests/categorySql/cleanUp_categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testCreateCategory() throws Exception {
        String requestBody = "{\"name\": \"category 1\", \"description\": \"desc 1\"}";

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))

                .andExpect(status().isOk());
    }

    @Test
    @SqlGroup({@Sql(scripts = "/sqlForControllerTests/categorySql/categories_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sqlForControllerTests/categorySql/cleanUp_categories.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    public void testUpdateCategory() throws Exception {
        String requestBody = "{\"id\":\"7\",\"name\": \"update 1\", \"description\": \"desc 1\"}";

        mockMvc.perform(put("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))

                .andExpect(status().isOk());
    }

    @Test
    @SqlGroup({@Sql(scripts = "/sqlForControllerTests/categorySql/categories_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sqlForControllerTests/categorySql/cleanUp_categories.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    public void testDeleteCategory() throws Exception {
        Long id = 7L;

        mockMvc.perform(delete("/api/categories/" + id)
        ).andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = "/sqlForControllerTests/categorySql/cleanUp_categories.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUploadCategoriesFromCsv() throws Exception {
        String content = "name,description\ncategory 1,desc 1\ncategory 2,desc 2";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "categories.csv",
                "text/csv",
                content.getBytes()
        );

        mockMvc.perform(multipart("/api/upload-categories").file(file)
        ).andExpect(status().isOk());
    }

    @Test
    @SqlGroup({@Sql(scripts = "/sqlForControllerTests/categorySql/categories_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sqlForControllerTests/categorySql/cleanUp_categories.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
    public void testSearchCategories() throws Exception {
        String name = "category";

        mockMvc.perform(get("/api/categories/search?name=" + name)).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("category 1"));
    }
}