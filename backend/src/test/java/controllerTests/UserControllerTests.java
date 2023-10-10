package controllerTests;


import com.alex.eshop.EshopApplication;
import com.alex.eshop.dto.userDTOs.UserDTO;
import com.alex.eshop.service.UserService;
import config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(classes = EshopApplication.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetUser() throws Exception {
        String userUuid = "f07f6f24-c0a8-4460-9dc6-c7147583394d"; //userUuid of test-user

        mockMvc.perform(get("/api/users/" + userUuid)).andExpect(status().isOk());
    }
}