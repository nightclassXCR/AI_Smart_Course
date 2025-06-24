package com.dd.ai_smart_course.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String validToken = "你真实系统中生成的有效token";

    @Test
    public void testCheckToken_ValidToken() throws Exception {
        mockMvc.perform(get("/token/normalCheck")
                        .param("token", validToken))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testCheckToken_InvalidToken() throws Exception {
        mockMvc.perform(get("/token/normalCheck")
                        .param("token", "invalid.or.expired.token"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
