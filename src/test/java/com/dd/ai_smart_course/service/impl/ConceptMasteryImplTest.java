package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.Concept_mastery;
import com.dd.ai_smart_course.mapper.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConceptMasteryImpl 服务单元测试")
class ConceptMasteryImplTest {

    @Mock
    private ConceptMasteryMapper conceptMasteryMapper;
    @Mock
    private LogMapper learnLogMapper; // 虽然在 calculateMasteryForUserConcept 中被注释，但作为依赖仍需Mock
    @Mock
    private ScoreMapper scoreMapper; // 同上
    @Mock
    private ConceptQuestionMapper conceptQuestionMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ConceptMapper conceptMapper;

    @InjectMocks
    private ConceptMasteryImpl conceptMasteryService;

    // 测试数据
    private Long userId1 = 1L;
    private Long userId2 = 2L;
    private Long conceptId1 = 101L;
    private Long conceptId2 = 102L;
    private Long taskId = 201L;

    private Concept_mastery existingMasteryForUser1Concept1;
    private Concept_mastery newMasteryForUser2Concept2;

    @BeforeEach
    void setUp() {
        // 初始化现有的掌握度对象
        existingMasteryForUser1Concept1 = new Concept_mastery(userId1, conceptId1, 75, LocalDateTime.now().minusDays(5));
        existingMasteryForUser1Concept1.setMastery_level(75.0); // 确保与字段类型匹配

        // 初始化一个新的掌握度对象（用于插入场景的参数）
        newMasteryForUser2Concept2 = new Concept_mastery(userId2, conceptId2, 50, LocalDateTime.now());
        newMasteryForUser2Concept2.setMastery_level(50.0); // 确保与字段类型匹配
    }

    @Test
    @DisplayName("recalculateAllMasteryLevels: 定时任务 - 成功更新和插入所有用户概念掌握度")
    void testRecalculateAllMasteryLevels_Success() {
        // 准备 (Given)
        List<Long> allUserIds = Arrays.asList(userId1, userId2);
        List<Long> allConceptIds = Arrays.asList(conceptId1, conceptId2);

        // 模拟 UserMapper 和 ConceptMapper 返回所有ID
        when(userMapper.findAllUserIds()).thenReturn(allUserIds);
        when(conceptMapper.findAllConceptIds()).thenReturn(allConceptIds);

        // 模拟 calculateMasteryForUserConcept 每次都返回一个固定值，以便测试流程
        // 注意：这里是对 calculateMasteryForUserConcept 方法的 "桩 (stub)"，而不是它的真实测试
        ConceptMasteryImpl spyService = spy(conceptMasteryService); // 使用 spy 来部分模拟
        doReturn(80).when(spyService).calculateMasteryForUserConcept(userId1, conceptId1);
        doReturn(60).when(spyService).calculateMasteryForUserConcept(userId1, conceptId2);
        doReturn(70).when(spyService).calculateMasteryForUserConcept(userId2, conceptId1);
        doReturn(90).when(spyService).calculateMasteryForUserConcept(userId2, conceptId2);


        // 模拟 findByUserIdAndConceptId 的行为：
        // 用户1-概念1已存在
        when(conceptMasteryMapper.findByUserIdAndConceptId(userId1, conceptId1))
                .thenReturn(Optional.of(existingMasteryForUser1Concept1));
        // 用户1-概念2不存在 (假设)
        when(conceptMasteryMapper.findByUserIdAndConceptId(userId1, conceptId2))
                .thenReturn(Optional.empty());
        // 用户2-概念1不存在 (假设)
        when(conceptMasteryMapper.findByUserIdAndConceptId(userId2, conceptId1))
                .thenReturn(Optional.empty());
        // 用户2-概念2已存在
        when(conceptMasteryMapper.findByUserIdAndConceptId(userId2, conceptId2))
                .thenReturn(Optional.of(new Concept_mastery(userId2, conceptId2, 85, LocalDateTime.now())));


        // When
        spyService.recalculateAllMasteryLevels(); // 调用带有 spy 的服务

        // Then
        // 验证 findAllUserIds 和 findAllConceptIds 被调用
        verify(userMapper, times(1)).findAllUserIds();
        verify(conceptMapper, times(1)).findAllConceptIds();

        // 验证 calculateMasteryForUserConcept 为每个用户和概念对调用
        verify(spyService, times(1)).calculateMasteryForUserConcept(userId1, conceptId1);
        verify(spyService, times(1)).calculateMasteryForUserConcept(userId1, conceptId2);
        verify(spyService, times(1)).calculateMasteryForUserConcept(userId2, conceptId1);
        verify(spyService, times(1)).calculateMasteryForUserConcept(userId2, conceptId2);
        verify(spyService, times(4)).calculateMasteryForUserConcept(anyLong(), anyLong());

        // 验证 findByUserIdAndConceptId 被调用 4 次
        verify(conceptMasteryMapper, times(4)).findByUserIdAndConceptId(anyLong(), anyLong());

        // 验证 updateConceptMastery 被调用 2 次 (userId1-conceptId1, userId2-conceptId2)
        verify(conceptMasteryMapper, times(2)).updateConceptMastery(any(Concept_mastery.class));
        // 验证 insertConceptMastery 被调用 2 次 (userId1-conceptId2, userId2-conceptId1)
        verify(conceptMasteryMapper, times(2)).insertConceptMastery(any(Concept_mastery.class));

        // 验证更新操作的参数（例如，掌握度级别和最近练习时间是否正确设置）
        ArgumentCaptor<Concept_mastery> updateCaptor = ArgumentCaptor.forClass(Concept_mastery.class);
        verify(conceptMasteryMapper, atLeast(1)).updateConceptMastery(updateCaptor.capture());
        List<Concept_mastery> updatedMasteries = updateCaptor.getAllValues();
        // 验证被更新的对象至少有一个的 mastery_level 是 80 (来自 userId1-conceptId1 的桩)
        assertTrue(updatedMasteries.stream().anyMatch(m -> m.getMastery_level() == 80.0), "应有掌握度被更新为80");
        // 验证被更新的对象至少有一个的 mastery_level 是 90 (来自 userId2-conceptId2 的桩)
        assertTrue(updatedMasteries.stream().anyMatch(m -> m.getMastery_level() == 90.0), "应有掌握度被更新为90");
        assertTrue(updatedMasteries.stream().allMatch(m -> m.getLast_practiced() != null), "更新的掌握度应设置最近练习时间");

        // 验证插入操作的参数
        ArgumentCaptor<Concept_mastery> insertCaptor = ArgumentCaptor.forClass(Concept_mastery.class);
        verify(conceptMasteryMapper, atLeast(1)).insertConceptMastery(insertCaptor.capture());
        List<Concept_mastery> insertedMasteries = insertCaptor.getAllValues();
        // 验证被插入的对象至少有一个的 mastery_level 是 60 (来自 userId1-conceptId2 的桩)
        assertTrue(insertedMasteries.stream().anyMatch(m -> m.getMastery_level() == 60.0), "应有掌握度被插入为60");
        // 验证被插入的对象至少有一个的 mastery_level 是 70 (来自 userId2-conceptId1 的桩)
        assertTrue(insertedMasteries.stream().anyMatch(m -> m.getMastery_level() == 70.0), "应有掌握度被插入为70");
        assertTrue(insertedMasteries.stream().allMatch(m -> m.getLast_practiced() != null), "插入的掌握度应设置最近练习时间");
    }

    @Test
    @DisplayName("recalculateAllMasteryLevels: 定时任务 - 处理空的用户列表或概念列表")
    void testRecalculateAllMasteryLevels_EmptyLists() {
        // 准备 (Given)
        when(userMapper.findAllUserIds()).thenReturn(Collections.emptyList());
        when(conceptMapper.findAllConceptIds()).thenReturn(Collections.emptyList());

        // When
        conceptMasteryService.recalculateAllMasteryLevels();

        // Then
        // 验证 UserMapper 和 ConceptMapper 被调用一次
        verify(userMapper, times(1)).findAllUserIds();
        verify(conceptMapper, times(1)).findAllConceptIds();

        // 验证没有进一步的 mapper 调用，因为列表为空
        verify(conceptMasteryMapper, never()).findByUserIdAndConceptId(anyLong(), anyLong());
        verify(conceptMasteryMapper, never()).updateConceptMastery(any(Concept_mastery.class));
        verify(conceptMasteryMapper, never()).insertConceptMastery(any(Concept_mastery.class));
    }


    @Test
    @DisplayName("calculateMasteryForUserConcept: 计算单个用户概念掌握度 - 返回值在有效范围内")
    void testCalculateMasteryForUserConcept_ReturnsValidRange() {
        // 准备 (Given)
        // 注意：由于服务实现中使用了 Math.random()，这里无法断言确切的返回值。
        // 我们只能测试其输出是否在预期范围内 (0-100)。
        // 如果实际业务逻辑是基于 mapper 调用的，则需要在此处模拟 mapper 的返回值。

        // 操作 (When)
        int masteryLevel = conceptMasteryService.calculateMasteryForUserConcept(userId1, conceptId1);

        // 验证 (Then)
        assertTrue(masteryLevel >= 0 && masteryLevel <= 100, "掌握度应在0到100之间");

        // 由于使用了 Math.random()，不会与任何 Mocks 交互，所以这里不进行 verify
        verifyNoInteractions(scoreMapper, learnLogMapper);
    }

    @Test
    @DisplayName("updateMasteryForTaskSubmission: 根据任务提交更新掌握度 - 成功更新和插入")
    void testUpdateMasteryForTaskSubmission_Success() {
        // 准备 (Given)
        List<Long> conceptIdsRelatedToTask = Arrays.asList(conceptId1, conceptId2);

        // 模拟 findConceptIdsByTaskId 返回相关概念ID
        when(conceptQuestionMapper.findConceptIdsByTaskId(taskId)).thenReturn(conceptIdsRelatedToTask);

        // 模拟 calculateMasteryForUserConcept 返回固定值
        ConceptMasteryImpl spyService = spy(conceptMasteryService);
        doReturn(85).when(spyService).calculateMasteryForUserConcept(userId1, conceptId1);
        doReturn(95).when(spyService).calculateMasteryForUserConcept(userId1, conceptId2);

        // 模拟 findByUserIdAndConceptId 的行为
        // 用户1-概念1已存在
        when(conceptMasteryMapper.findByUserIdAndConceptId(userId1, conceptId1))
                .thenReturn(Optional.of(existingMasteryForUser1Concept1));
        // 用户1-概念2不存在 (新记录)
        when(conceptMasteryMapper.findByUserIdAndConceptId(userId1, conceptId2))
                .thenReturn(Optional.empty());


        // 操作 (When)
        spyService.updateMasteryForTaskSubmission(userId1, taskId);

        // 验证 (Then)
        // 验证 findConceptIdsByTaskId 被调用一次
        verify(conceptQuestionMapper, times(1)).findConceptIdsByTaskId(taskId);

        // 验证 calculateMasteryForUserConcept 为每个相关概念调用
        verify(spyService, times(1)).calculateMasteryForUserConcept(userId1, conceptId1);
        verify(spyService, times(1)).calculateMasteryForUserConcept(userId1, conceptId2);

        // 验证 findByUserIdAndConceptId 为每个相关概念调用
        verify(conceptMasteryMapper, times(1)).findByUserIdAndConceptId(userId1, conceptId1);
        verify(conceptMasteryMapper, times(1)).findByUserIdAndConceptId(userId1, conceptId2);

        // 验证 updateConceptMastery 被调用一次 (针对 conceptId1)
        ArgumentCaptor<Concept_mastery> updateCaptor = ArgumentCaptor.forClass(Concept_mastery.class);
        verify(conceptMasteryMapper, times(1)).updateConceptMastery(updateCaptor.capture());
        Concept_mastery updatedMastery = updateCaptor.getValue();
        assertEquals(userId1, updatedMastery.getUser_id());
        assertEquals(conceptId1, updatedMastery.getConcept_id());
        assertEquals(85.0, updatedMastery.getMastery_level()); // 验证更新的值
        assertNotNull(updatedMastery.getLast_practiced());

        // 验证 insertConceptMastery 被调用一次 (针对 conceptId2)
        ArgumentCaptor<Concept_mastery> insertCaptor = ArgumentCaptor.forClass(Concept_mastery.class);
        verify(conceptMasteryMapper, times(1)).insertConceptMastery(insertCaptor.capture());
        Concept_mastery insertedMastery = insertCaptor.getValue();
        assertEquals(userId1, insertedMastery.getUser_id());
        assertEquals(conceptId2, insertedMastery.getConcept_id());
        assertEquals(95.0, insertedMastery.getMastery_level()); // 验证插入的值
        assertNotNull(insertedMastery.getLast_practiced());
    }

    @Test
    @DisplayName("updateMasteryForTaskSubmission: 根据任务提交更新掌握度 - 任务无关联概念")
    void testUpdateMasteryForTaskSubmission_NoRelatedConcepts() {
        // 准备 (Given)
        // 模拟 findConceptIdsByTaskId 返回空列表
        when(conceptQuestionMapper.findConceptIdsByTaskId(taskId)).thenReturn(Collections.emptyList());

        // 操作 (When)
        conceptMasteryService.updateMasteryForTaskSubmission(userId1, taskId);

        // 验证 (Then)
        // 验证 findConceptIdsByTaskId 被调用一次
        verify(conceptQuestionMapper, times(1)).findConceptIdsByTaskId(taskId);

        // 验证没有进一步的 mapper 调用，因为没有关联概念
        verify(conceptMasteryMapper, never()).findByUserIdAndConceptId(anyLong(), anyLong());
        verify(conceptMasteryMapper, never()).updateConceptMastery(any(Concept_mastery.class));
        verify(conceptMasteryMapper, never()).insertConceptMastery(any(Concept_mastery.class));
    }
}