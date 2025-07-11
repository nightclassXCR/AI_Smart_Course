package com.dd.ai_smart_course.ControllerTest;

import com.dd.ai_smart_course.controller.ScoreController;
import com.dd.ai_smart_course.entity.Score;
import com.dd.ai_smart_course.service.base.ScoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ScoreControllerTest {

    private MockMvc mockMvc;
    private ScoreService scoreService;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws Exception {
        scoreService = mock(ScoreService.class);

        ScoreController controller = new ScoreController();

        // 手动注入 mock 的 ScoreService
        var field = ScoreController.class.getDeclaredField("scoreService");
        field.setAccessible(true);
        field.set(controller, scoreService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetById() throws Exception {
        Score score = new Score();
        score.setId(1);
        when(scoreService.getById(1)).thenReturn(score);

        mockMvc.perform(get("/score/score/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    public void testInsert() throws Exception {
        List<Score> scores = List.of(new Score());

        doNothing().when(scoreService).insertBatch(any());

        mockMvc.perform(post("/score/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(scores)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("操作成功"));
    }

    @Test
    public void testDelete() throws Exception {
        List<Integer> ids = List.of(1, 2, 3);
        doNothing().when(scoreService).deleteBatch(any());

        mockMvc.perform(delete("/score/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ids)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("操作成功"));
    }

    @Test
    public void testUpdate() throws Exception {
        Score score = new Score();
        doNothing().when(scoreService).update(any());

        mockMvc.perform(put("/score/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(score)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("操作成功"));
    }

}
