package controllerTests;

import com.alex.eshop.EshopApplication;
import config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(classes = EshopApplication.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetStats() throws Exception {
        mockMvc.perform(get("/api/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[5].category").value("Smart Home"))
                .andExpect(jsonPath("$.content[5].itemsCount").value(3))
                .andDo(print());
    }
}