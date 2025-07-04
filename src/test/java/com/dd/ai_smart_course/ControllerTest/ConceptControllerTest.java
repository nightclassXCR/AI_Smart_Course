package com.dd.ai_smart_course.ControllerTest;

import com.dd.ai_smart_course.controller.ConceptController;
import com.dd.ai_smart_course.dto.ConceptDTO;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Question;
import com.dd.ai_smart_course.service.base.ConceptService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")  // 如果你有测试环境配置可以加
public class ConceptControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Mock
    private ConceptService conceptService;

    @InjectMocks
    private ConceptController conceptController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // 用 MockMvcBuilders 构建 MockMvc，注入我们手动构造的 Controller
        mockMvc = MockMvcBuilders.standaloneSetup(conceptController).build();
    }

    @Test
    public void testGetAllConcepts() throws Exception {
        when(conceptService.getAllConcepts()).thenReturn(List.of(new Concept()));
        mockMvc.perform(get("/concepts"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetConceptsByChapterId() throws Exception {
        when(conceptService.getConceptsByChapterId(1)).thenReturn(List.of(new Concept()));
        mockMvc.perform(get("/concepts/by-chapter/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddConcept() throws Exception {
        ConceptDTO dto = new ConceptDTO();
        mockMvc.perform(post("/concepts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateConcept() throws Exception {
        ConceptDTO dto = new ConceptDTO();
        mockMvc.perform(put("/concepts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteConceptSuccess() throws Exception {
        when(conceptService.deleteConcept(1)).thenReturn(1);
        mockMvc.perform(delete("/concepts/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteConceptFailure() throws Exception {
        when(conceptService.deleteConcept(1)).thenReturn(0);
        mockMvc.perform(delete("/concepts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("删除失败"));
    }

    @Test
    public void testLinkConceptToQuestion() throws Exception {
        mockMvc.perform(post("/concepts/1/questions/2"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetQuestionsByConcept() throws Exception {
        when(conceptService.getQuestionsByConcept(1)).thenReturn(List.of(new Question()));
        mockMvc.perform(get("/concepts/1/questions"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateMasteryLevel() throws Exception {
        mockMvc.perform(put("/concepts/users/1/concepts/2/mastery")
                        .param("masteryLevel", "3"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetMasteryLevel() throws Exception {
        when(conceptService.getMasteryLevel(1, 2)).thenReturn(5);
        mockMvc.perform(get("/concepts/users/1/concepts/2/mastery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(5));
    }
}
