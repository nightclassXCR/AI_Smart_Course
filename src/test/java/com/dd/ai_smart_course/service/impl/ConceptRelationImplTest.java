package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.ConceptRelation;
import com.dd.ai_smart_course.mapper.ConceptRelationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConceptRelationImpl 服务单元测试")
class ConceptRelationImplTest {

    @Mock
    private ConceptRelationMapper mapper; // 模拟 Mapper 依赖

    @InjectMocks
    private ConceptRelationImpl conceptRelationService; // 注入模拟对象到服务实现中

    private ConceptRelation testRelation;
    private ConceptRelation anotherRelation;

    @BeforeEach
    void setUp() {
        // 在每个测试方法运行前，初始化测试数据
        testRelation = new ConceptRelation();
        testRelation.setId(1);
        testRelation.setSourceConceptId(101);
        testRelation.setTargetConceptId(102);
        testRelation.setRelationType("prerequisite"); // "前置条件" 关系

        anotherRelation = new ConceptRelation();
        anotherRelation.setId(2);
        anotherRelation.setSourceConceptId(101);
        anotherRelation.setTargetConceptId(103);
        anotherRelation.setRelationType("related"); // "相关" 关系
    }

    @Test
    @DisplayName("测试添加关系 - 成功场景")
    void testAddRelation_成功() {
        // 准备 (Given)
        int expectedResult = 1; // 期望 mapper 返回 1，表示插入成功
        when(mapper.addRelation(any(ConceptRelation.class))).thenReturn(expectedResult);

        // 操作 (When)
        int actualResult = conceptRelationService.addRelation(testRelation);

        // 验证 (Then)
        assertEquals(expectedResult, actualResult, "返回值应为1，表示成功");
        verify(mapper, times(1)).addRelation(testRelation); // 验证 mapper 的 addRelation 方法被调用了一次
    }

    @Test
    @DisplayName("测试添加关系 - 失败场景")
    void testAddRelation_失败() {
        // 准备 (Given)
        int expectedResult = 0; // 期望 mapper 返回 0，表示插入失败
        when(mapper.addRelation(any(ConceptRelation.class))).thenReturn(expectedResult);

        // 操作 (When)
        int actualResult = conceptRelationService.addRelation(testRelation);

        // 验证 (Then)
        assertEquals(expectedResult, actualResult, "返回值应为0，表示失败");
        verify(mapper, times(1)).addRelation(testRelation); // 验证 mapper 的 addRelation 方法仍然被调用了
    }

    @Test
    @DisplayName("测试删除关系 - 成功场景")
    void testDeleteRelation_成功() {
        // 准备 (Given)
        int relationId = 1;
        int expectedResult = 1; // 期望 mapper 返回 1，表示删除成功
        when(mapper.deleteRelation(relationId)).thenReturn(expectedResult);

        // 操作 (When)
        int actualResult = conceptRelationService.deleteRelation(relationId);

        // 验证 (Then)
        assertEquals(expectedResult, actualResult, "返回值应为1，表示成功");
        verify(mapper, times(1)).deleteRelation(relationId); // 验证 deleteRelation 方法被以正确的 ID 调用
    }

    @Test
    @DisplayName("测试删除关系 - 未找到或删除失败")
    void testDeleteRelation_未找到() {
        // 准备 (Given)
        int relationId = 999;
        int expectedResult = 0; // 期望 mapper 返回 0，表示没有找到对应记录或删除失败
        when(mapper.deleteRelation(relationId)).thenReturn(expectedResult);

        // 操作 (When)
        int actualResult = conceptRelationService.deleteRelation(relationId);

        // 验证 (Then)
        assertEquals(expectedResult, actualResult, "返回值应为0，表示失败");
        verify(mapper, times(1)).deleteRelation(relationId);
    }

    @Test
    @DisplayName("测试根据概念ID获取关系 - 查找到结果")
    void testGetRelationsByConcept_有结果() {
        // 准备 (Given)
        int conceptId = 101;
        List<ConceptRelation> expectedRelations = Arrays.asList(testRelation, anotherRelation);
        when(mapper.getRelationsByConcept(conceptId)).thenReturn(expectedRelations);

        // 操作 (When)
        List<ConceptRelation> actualRelations = conceptRelationService.getRelationsByConcept(conceptId);

        // 验证 (Then)
        assertNotNull(actualRelations, "返回的列表不应为 null");
        assertEquals(2, actualRelations.size(), "返回的列表应包含两个元素");
        assertEquals(expectedRelations, actualRelations, "返回的列表应与预期相符");
        assertTrue(actualRelations.stream().allMatch(r -> r.getSourceConceptId() == conceptId), "所有关系都应源于给定的概念ID");
        verify(mapper, times(1)).getRelationsByConcept(conceptId); // 验证 getRelationsByConcept 方法被以正确的 ID 调用
    }

    @Test
    @DisplayName("测试根据概念ID获取关系 - 未查找到结果")
    void testGetRelationsByConcept_无结果() {
        // 准备 (Given)
        int conceptId = 999;
        when(mapper.getRelationsByConcept(conceptId)).thenReturn(Collections.emptyList()); // 模拟返回一个空列表

        // 操作 (When)
        List<ConceptRelation> actualRelations = conceptRelationService.getRelationsByConcept(conceptId);

        // 验证 (Then)
        assertNotNull(actualRelations, "返回的列表不应为 null");
        assertTrue(actualRelations.isEmpty(), "返回的列表应为空");
        verify(mapper, times(1)).getRelationsByConcept(conceptId);
    }
}