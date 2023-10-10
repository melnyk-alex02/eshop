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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(classes = EshopApplication.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class ItemControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @SqlGroup({
            @Sql(scripts = "/sqlForControllerTests/itemSql/items_table.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sqlForControllerTests/itemSql/cleanUp_items.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void testGetAllItems() throws Exception {
        mockMvc.perform(get("/api/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.content[18].id").value(19L))
                .andExpect(jsonPath("$.content[18].name").value("item 1"))
                .andExpect(jsonPath("$.content[18].description").value("desc 1"))
                .andExpect(jsonPath("$.content[18].categoryId").value(1L))
                .andExpect(jsonPath("$.content[18].imageSrc").value("img 1"))

                .andExpect(jsonPath("$.content[19].id").value(20L))
                .andExpect(jsonPath("$.content[19].name").value("item 2"))
                .andExpect(jsonPath("$.content[19].description").value("desc 2"))
                .andExpect(jsonPath("$.content[19].categoryId").value(2L))
                .andExpect(jsonPath("$.content[19].imageSrc").value("img 2"));
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "/sqlForControllerTests/itemSql/items_table.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sqlForControllerTests/itemSql/cleanUp_items.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void testGetLastFiveItems() throws Exception {
        mockMvc.perform(get("/api/items/last")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(20L));
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "/sqlForControllerTests/itemSql/items_table.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sqlForControllerTests/itemSql/cleanUp_items.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void testGetItemsInCategory() throws Exception {
        mockMvc.perform(get("/api/item?categoryId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = "/sqlForControllerTests/itemSql/cleanUp_items.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testCreateItem() throws Exception {
        String requestBody = "{\"name\": \"item 1\", " +
                "\"description\": \"desc 1\"," +
                " \"categoryId\":\"1\", " +
                "\"imageSrc\":\"img 1\"}";

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))

                .andExpect(status().isOk());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "/sqlForControllerTests/itemSql/items_table.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sqlForControllerTests/itemSql/cleanUp_items.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void testUpdateItem() throws Exception {
        String requestBody = "{\"id\":\"19\"," +
                "\"name\": \"update 1\", " +
                "\"description\": \"desc 1\"," +
                " \"categoryId\":\"1\", " +
                "\"imageSrc\":\"img 1\"}";

        mockMvc.perform(put("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))

                .andExpect(status().isOk());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "/sqlForControllerTests/itemSql/items_table.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sqlForControllerTests/itemSql/cleanUp_items.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void testDeleteItem() throws Exception {
        Long id = 19L;
        mockMvc.perform(delete("/api/items/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = "/sqlForControllerTests/itemSql/cleanUp_items.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUploadItemsFromCsv() throws Exception {
        String content = "name,description,categoryId,imageSrc\nitem 1,desc 1,1,img 1\nitem 2,desc 2,2,img 2";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "items.csv",
                "text/csv",
                content.getBytes()
        );

        mockMvc.perform(multipart("/api/upload-items").file(file)).andExpect(status().isOk());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "/sqlForControllerTests/itemSql/items_table.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "/sqlForControllerTests/itemSql/cleanUp_items.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void testSearchItems() throws Exception {
        String name = "item";
        Boolean hasImage = true;

        mockMvc.perform(get("/api/items/search?page=0&name=" + name + "&hasImage=" + hasImage))
                .andExpect(status().isOk());
    }
}