package com.dd.ai_smart_course.ControllerTest;

import com.dd.ai_smart_course.entity.ConceptRelation;
import com.dd.ai_smart_course.mapper.ConceptRelationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConceptRelationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConceptRelationMapper conceptRelationMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private static int insertedId;

    @Test
    @Order(1)
    public void testAddRelation() throws Exception {
        ConceptRelation relation = new ConceptRelation();
        relation.setSourceConceptId(1);
        relation.setTargetConceptId(2);
        relation.setRelationType("prerequisite");

        String json = objectMapper.writeValueAsString(relation);

        mockMvc.perform(post("/concept-relations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(matchesRegex("\\d+"))); // 返回插入 ID

        // 查询刚插入的对象获取 ID
        List<ConceptRelation> list = conceptRelationMapper.getRelationsByConcept(1);
        Assertions.assertFalse(list.isEmpty());
        insertedId = list.get(list.size() - 1).getId();
    }

    @Test
    @Order(2)
    public void testGetRelationsByConcept() throws Exception {
        mockMvc.perform(get("/concept-relations/concept/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(3)
    public void testDeleteRelation() throws Exception {
        mockMvc.perform(delete("/concept-relations/" + insertedId))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }
}
