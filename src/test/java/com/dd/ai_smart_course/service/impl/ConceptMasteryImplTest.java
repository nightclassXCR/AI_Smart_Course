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
import static org.mockito.ArgumentMatchers.anyInt; // Changed from anyLong() to anyInt()
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
    private int userId1 = 1;
    private int userId2 = 2;
    private int conceptId1 = 101;
    private int conceptId2 = 102;
    private int taskId = 201;

    private Concept_mastery existingMasteryForUser1Concept1;
    private Concept_mastery newMasteryForUser2Concept2; // This object is conceptually "new" for an insert scenario.

    @BeforeEach
    void setUp() {
        // 初始化现有的掌握度对象
        existingMasteryForUser1Concept1 = new Concept_mastery(userId1, conceptId1, 75, LocalDateTime.now().minusDays(5));
        existingMasteryForUser1Concept1.setMastery_level(75.0); // 确保与字段类型匹配

        // 初始化一个新的掌握度对象（用于插入场景的参数，或者作为另一个已存在对象）
        // 这里只是为了演示一个Concept_mastery对象，它可能在测试中作为已存在的或待插入的
        newMasteryForUser2Concept2 = new Concept_mastery(userId2, conceptId2, 50, LocalDateTime.now());
        newMasteryForUser2Concept2.setMastery_level(50.0); // 确保与字段类型匹配
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
        verify(conceptMasteryMapper, never()).findByUserIdAndConceptId(anyInt(), anyInt()); // 使用 anyInt()
        verify(conceptMasteryMapper, never()).updateConceptMastery(any(Concept_mastery.class));
        verify(conceptMasteryMapper, never()).insertConceptMastery(any(Concept_mastery.class));

        // 验证 calculateMasteryForUserConcept 没有被调用
        // 注意：这里需要 spy 才能验证内部方法
        ConceptMasteryImpl spyService = spy(conceptMasteryService);
        // Call the method on the spy
        spyService.recalculateAllMasteryLevels();
        verify(spyService, never()).calculateMasteryForUserConcept(anyInt(), anyInt());
    }

    @Test
    @DisplayName("calculateMasteryForUserConcept: 计算单个用户概念掌握度 - 返回值在有效范围内")
    void testCalculateMasteryForUserConcept_ReturnsValidRange() {
        // 准备 (Given)
        // 当前实现中 calculateMasteryForUserConcept 不与任何 Mocked Mapper 交互。
        // 它完全依赖于 Math.random()。

        // 操作 (When)
        int masteryLevel = conceptMasteryService.calculateMasteryForUserConcept(userId1, conceptId1);

        // 验证 (Then)
        assertTrue(masteryLevel >= 0 && masteryLevel <= 100, "掌握度应在0到100之间");

        // 由于使用了 Math.random()，不会与任何 Mocks 交互，所以这里不进行 verify 与 mocks 的交互。
        // 明确验证没有不必要的交互。
        verifyNoInteractions(scoreMapper, learnLogMapper, conceptQuestionMapper, userMapper, conceptMapper, conceptMasteryMapper);
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
        verify(conceptMasteryMapper, never()).findByUserIdAndConceptId(anyInt(), anyInt()); // 使用 anyInt()
        verify(conceptMasteryMapper, never()).updateConceptMastery(any(Concept_mastery.class));
        verify(conceptMasteryMapper, never()).insertConceptMastery(any(Concept_mastery.class));

        // 验证 calculateMasteryForUserConcept 没有被调用
        // 这需要一个 spy，因为 calculateMasteryForUserConcept 是 ConceptMasteryImpl 的内部方法
        ConceptMasteryImpl spyService = spy(conceptMasteryService);
        // Re-call the method on the spy
        spyService.updateMasteryForTaskSubmission(userId1, taskId);
        verify(spyService, never()).calculateMasteryForUserConcept(anyInt(), anyInt());
    }
}