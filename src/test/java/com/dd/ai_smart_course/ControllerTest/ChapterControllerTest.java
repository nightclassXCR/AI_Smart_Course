package com.dd.ai_smart_course.ControllerTest;

import com.dd.ai_smart_course.controller.ChapterController;
import com.dd.ai_smart_course.dto.ChapterDTO;
import com.dd.ai_smart_course.entity.Chapter;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.service.base.ChapterService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ChapterControllerTest {

    private MockMvc mockMvc;
    private ChapterService chapterService;
    private ObjectMapper objectMapper;
    private ChapterController chapterController;

    @BeforeEach
    public void setUp() {
        chapterService = mock(ChapterService.class); // 手动 mock
        objectMapper = new ObjectMapper();
        chapterController = new ChapterController();

        // 手动注入
        var chapterField = ChapterController.class.getDeclaredFields();
        for (var field : chapterField) {
            if (field.getType() == ChapterService.class) {
                field.setAccessible(true);
                try {
                    field.set(chapterController, chapterService);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        mockMvc = MockMvcBuilders.standaloneSetup(chapterController).build();
    }

    @Test
    public void testGetAllChapters() throws Exception {
        when(chapterService.getAllChapter()).thenReturn(List.of(new Chapter()));

        mockMvc.perform(get("/chapters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("获取成功"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetChapterByCourseId() throws Exception {
        when(chapterService.getChaptersByCourseId(1)).thenReturn(List.of(new Chapter()));

        mockMvc.perform(get("/chapters/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("获取成功"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testAddChapterSuccess() throws Exception {
        when(chapterService.addChapter(any())).thenReturn(1);
        ChapterDTO dto = new ChapterDTO();

        mockMvc.perform(post("/chapters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("添加成功"));
    }

    @Test
    public void testAddChapterFail() throws Exception {
        when(chapterService.addChapter(any())).thenReturn(0);
        ChapterDTO dto = new ChapterDTO();

        mockMvc.perform(post("/chapters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("添加失败"));
    }

    @Test
    public void testDeleteChapter() throws Exception {
        when(chapterService.deleteChapter(1)).thenReturn(1);

        mockMvc.perform(delete("/chapters/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("删除成功"));
    }

    @Test
    public void testReorderChapters() throws Exception {
        doNothing().when(chapterService).reorderChapters((int) eq(1L), anyList());

        mockMvc.perform(put("/chapters/reorder/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(1L, 2L, 3L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("排序成功"));
    }

    @Test
    public void testGetConceptsByChapterId() throws Exception {
        when(chapterService.getConceptsByChapterId(1)).thenReturn(List.of(new Concept()));

        mockMvc.perform(get("/chapters/1/concepts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("获取成功"))
                .andExpect(jsonPath("$.data").isArray());
    }
}
