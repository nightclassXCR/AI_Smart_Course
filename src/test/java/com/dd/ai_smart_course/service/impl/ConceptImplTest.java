package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.dto.ConceptDTO;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Question;
import com.dd.ai_smart_course.event.LearningActionEvent;
import com.dd.ai_smart_course.mapper.ChapterMapper;
import com.dd.ai_smart_course.mapper.ConceptMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * ConceptImpl 服务层单元测试
 * 使用 Mockito 模拟数据库操作和事件发布，无需预先准备测试数据
 */
@ExtendWith(MockitoExtension.class)
class ConceptImplTest {

    @Mock
    private ConceptMapper conceptMapper;

    @Mock
    private ChapterMapper chapterMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ConceptImpl conceptService;

    private Concept testConcept;
    private ConceptDTO testConceptDTO;
    private List<Concept> testConceptList;
    private List<Question> testQuestionList;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testConcept = new Concept();
        testConcept.setId(1);
        testConcept.setChapterId(100);
        testConcept.setName("Java变量");
        testConcept.setDescription("Java变量的概念和使用方法");

        testConceptDTO = new ConceptDTO(testConcept, "第一章 Java基础");
        testConceptDTO.setId(1);
        testConceptDTO.setChapterName("第一章 Java基础");
        testConceptDTO.setName("Java变量");
        testConceptDTO.setDescription("Java变量的概念和使用方法");

        testConceptList = Arrays.asList(
                createConcept(1L, 100, "Java变量", "变量概念"),
                createConcept(2L, 100, "数据类型", "数据类型概念"),
                createConcept(3L, 100, "运算符", "运算符概念")
        );

        testQuestionList = Arrays.asList(
                createQuestion(1L, "什么是变量？", "概念题"),
                createQuestion(2L, "如何声明变量？", "操作题")
        );
    }

    /**
     * 测试查询所有概念
     */
    @Test
    void testGetAllConcepts() {
        // Given
        when(conceptMapper.getAllConcepts()).thenReturn(testConceptList);

        // When
        List<Concept> result = conceptService.getAllConcepts();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Java变量", result.get(0).getName());
        verify(conceptMapper, times(1)).getAllConcepts();
    }

    /**
     * 测试根据章节ID查询概念
     */
    @Test
    void testGetConceptsByChapterId() {
        // Given
        int chapterId = 100;
        when(conceptMapper.getConceptsByChapterId(chapterId)).thenReturn(testConceptList);

        // When
        List<Concept> result = conceptService.getConceptsByChapterId(chapterId);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(100, result.get(0).getChapterId());
        verify(conceptMapper, times(1)).getConceptsByChapterId(chapterId);
    }

    /**
     * 测试添加概念 - 成功场景
     */
    @Test
    void testAddConcept_Success() {
        // Given
        when(chapterMapper.getCourseIdByCourseName("第一章 Java基础")).thenReturn(100);
        when(conceptMapper.addConcept(any(Concept.class))).thenReturn(1);

        // When
        int result = conceptService.addConcept(testConceptDTO);

        // Then
        assertEquals(1, result);
        verify(chapterMapper, times(1)).getCourseIdByCourseName("第一章 Java基础");
        verify(conceptMapper, times(1)).addConcept(any(Concept.class));
    }

    /**
     * 测试添加概念 - 章节不存在
     */
    @Test
    void testAddConcept_ChapterNotExists() {
        // Given
        when(chapterMapper.getCourseIdByCourseName("不存在的章节")).thenReturn(null);
        testConceptDTO.setChapterName("不存在的章节");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> conceptService.addConcept(testConceptDTO));
        assertEquals("课程不存在", exception.getMessage());
        verify(chapterMapper, times(1)).getCourseIdByCourseName("不存在的章节");
        verify(conceptMapper, never()).addConcept(any());
    }

    /**
     * 测试更新概念 - 成功场景
     */
    @Test
    void testUpdateConcept_Success() {
        // Given
        when(chapterMapper.getCourseIdByCourseName("第一章 Java基础")).thenReturn(100);
        when(conceptMapper.updateConcept(any(Concept.class))).thenReturn(1);

        // When
        int result = conceptService.updateConcept(testConceptDTO);

        // Then
        assertEquals(1, result);
        verify(chapterMapper, times(1)).getCourseIdByCourseName("第一章 Java基础");
        verify(conceptMapper, times(1)).updateConcept(any(Concept.class));
    }

    /**
     * 测试更新概念 - 章节不存在
     */
    @Test
    void testUpdateConcept_ChapterNotExists() {
        // Given
        when(chapterMapper.getCourseIdByCourseName("第一章 Java基础")).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> conceptService.updateConcept(testConceptDTO));
        assertEquals("课程不存在：第一章 Java基础", exception.getMessage());
        verify(conceptMapper, never()).updateConcept(any());
    }

    /**
     * 测试删除概念
     */
    @Test
    void testDeleteConcept() {
        // Given
        int conceptId = 1;
        when(conceptMapper.deleteConcept(conceptId)).thenReturn(1);

        // When
        int result = conceptService.deleteConcept(conceptId);

        // Then
        assertEquals(1, result);
        verify(conceptMapper, times(1)).deleteConcept(conceptId);
    }

    /**
     * 测试关联概念到题目
     */
    @Test
    void testLinkConceptToQuestion() {
        // Given
        Long conceptId = 1L;
        Long questionId = 100L;

        // When
        conceptService.linkConceptToQuestion(conceptId, questionId);

        // Then
        verify(conceptMapper, times(1)).linkConceptToQuestion(conceptId, questionId);
    }

    /**
     * 测试根据概念获取题目
     */
    @Test
    void testGetQuestionsByConcept() {
        // Given
        Long conceptId = 1L;
        when(conceptMapper.getQuestionsByConcept(conceptId)).thenReturn(testQuestionList);

        // When
        List<Question> result = conceptService.getQuestionsByConcept(conceptId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("什么是变量？", result.get(0).getContent());
        verify(conceptMapper, times(1)).getQuestionsByConcept(conceptId);
    }

    /**
     * 测试更新掌握程度
     */
    @Test
    void testUpdateMasteryLevel() {
        // Given
        Long userId = 1001L;
        Long conceptId = 1L;
        int masteryLevel = 80;

        // When
        conceptService.updateMasteryLevel(userId, conceptId, masteryLevel);

        // Then
        verify(conceptMapper, times(1)).updateMasteryLevel(userId, conceptId, masteryLevel);
    }

    /**
     * 测试获取掌握程度 - 有数据
     */
    @Test
    void testGetMasteryLevel_HasData() {
        // Given
        Long userId = 1001L;
        Long conceptId = 1L;
        when(conceptMapper.getMasteryLevel(userId, conceptId)).thenReturn(75);

        // When
        int result = conceptService.getMasteryLevel(userId, conceptId);

        // Then
        assertEquals(75, result);
        verify(conceptMapper, times(1)).getMasteryLevel(userId, conceptId);
    }

    /**
     * 测试获取掌握程度 - 无数据（返回默认值）
     */
    @Test
    void testGetMasteryLevel_NoData() {
        // Given
        Long userId = 1001L;
        Long conceptId = 1L;
        when(conceptMapper.getMasteryLevel(userId, conceptId)).thenReturn(null);

        // When
        int result = conceptService.getMasteryLevel(userId, conceptId);

        // Then
        assertEquals(0, result);
        verify(conceptMapper, times(1)).getMasteryLevel(userId, conceptId);
    }

    /**
     * 测试查看概念 - 成功场景
     */
    @Test
    void testViewConcept_Success() {
        // Given
        Long conceptId = 1L;
        Long userId = 1001L;
        Integer durationSeconds = 120;

        when(conceptMapper.findById(conceptId)).thenReturn(Optional.of(testConcept));

        // When
        conceptService.viewConcept(conceptId, userId, durationSeconds);

        // Then
        verify(conceptMapper, times(1)).findById(conceptId);

        // 验证事件发布
        ArgumentCaptor<LearningActionEvent> eventCaptor = ArgumentCaptor.forClass(LearningActionEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

        LearningActionEvent capturedEvent = eventCaptor.getValue();
        assertEquals(userId.intValue(), capturedEvent.getUserId());
        assertEquals("concept", capturedEvent.getTargetType());
        assertEquals(conceptId, capturedEvent.getTargetId());
        assertEquals("view", capturedEvent.getActionType());
        assertEquals(durationSeconds, capturedEvent.getDuration());
        assertTrue(capturedEvent.getDetail().contains("VIEW_CONCEPT"));
    }

    /**
     * 测试查看概念 - 概念不存在
     */
    @Test
    void testViewConcept_ConceptNotFound() {
        // Given
        Long conceptId = 999L;
        Long userId = 1001L;
        Integer durationSeconds = 120;

        when(conceptMapper.findById(conceptId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> conceptService.viewConcept(conceptId, userId, durationSeconds));
        assertEquals("Concept not found: " + conceptId, exception.getMessage());

        verify(conceptMapper, times(1)).findById(conceptId);
        verify(eventPublisher, never()).publishEvent(any());
    }

    /**
     * 测试标记概念为已掌握 - 成功场景
     */
    @Test
    void testMarkConceptAsMastered_Success() {
        // Given
        Long conceptId = 1L;
        Long userId = 1001L;

        when(conceptMapper.findById(conceptId)).thenReturn(Optional.of(testConcept));

        // When
        conceptService.markConceptAsMastered(conceptId, userId);

        // Then
        verify(conceptMapper, times(1)).findById(conceptId);

        // 验证事件发布
        ArgumentCaptor<LearningActionEvent> eventCaptor = ArgumentCaptor.forClass(LearningActionEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

        LearningActionEvent capturedEvent = eventCaptor.getValue();
        assertEquals(userId.intValue(), capturedEvent.getUserId());
        assertEquals("concept", capturedEvent.getTargetType());
        assertEquals(conceptId, capturedEvent.getTargetId());
        assertEquals("click", capturedEvent.getActionType());
        assertNull(capturedEvent.getDuration());
        assertTrue(capturedEvent.getDetail().contains("MARK_MASTERED"));
    }

    /**
     * 测试标记概念为已掌握 - 概念不存在
     */
    @Test
    void testMarkConceptAsMastered_ConceptNotFound() {
        // Given
        Long conceptId = 999L;
        Long userId = 1001L;

        when(conceptMapper.findById(conceptId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> conceptService.markConceptAsMastered(conceptId, userId));
        assertEquals("Concept not found: " + conceptId, exception.getMessage());

        verify(conceptMapper, times(1)).findById(conceptId);
        verify(eventPublisher, never()).publishEvent(any());
    }

    /**
     * 测试开始概念复习 - 成功场景
     */
    @Test
    void testStartConceptReview_Success() {
        // Given
        Long conceptId = 1L;
        Long userId = 1001L;

        when(conceptMapper.findById(conceptId)).thenReturn(Optional.of(testConcept));

        // When
        conceptService.startConceptReview(conceptId, userId);

        // Then
        verify(conceptMapper, times(1)).findById(conceptId);

        // 验证事件发布
        ArgumentCaptor<LearningActionEvent> eventCaptor = ArgumentCaptor.forClass(LearningActionEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

        LearningActionEvent capturedEvent = eventCaptor.getValue();
        assertEquals(userId.intValue(), capturedEvent.getUserId());
        assertEquals("concept", capturedEvent.getTargetType());
        assertEquals(conceptId, capturedEvent.getTargetId());
        assertEquals("click", capturedEvent.getActionType());
        assertNull(capturedEvent.getDuration());
        assertTrue(capturedEvent.getDetail().contains("START_REVIEW"));
    }

    /**
     * 测试开始概念复习 - 概念不存在
     */
    @Test
    void testStartConceptReview_ConceptNotFound() {
        // Given
        Long conceptId = 999L;
        Long userId = 1001L;

        when(conceptMapper.findById(conceptId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> conceptService.startConceptReview(conceptId, userId));
        assertEquals("Concept not found: " + conceptId, exception.getMessage());

        verify(conceptMapper, times(1)).findById(conceptId);
        verify(eventPublisher, never()).publishEvent(any());
    }

    /**
     * 测试事件发布的详细参数验证
     */
    @Test
    void testEventPublishing_DetailedValidation() {
        // Given
        Long conceptId = 1L;
        Long userId = 1001L;
        Integer durationSeconds = 180;

        when(conceptMapper.findById(conceptId)).thenReturn(Optional.of(testConcept));

        // When
        conceptService.viewConcept(conceptId, userId, durationSeconds);

        // Then
        ArgumentCaptor<LearningActionEvent> eventCaptor = ArgumentCaptor.forClass(LearningActionEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());

        LearningActionEvent event = eventCaptor.getValue();

        // 详细验证事件参数
        assertNotNull(event.getSource());
        assertEquals(conceptService, event.getSource());
        assertEquals(1001, event.getUserId());
        assertEquals("concept", event.getTargetType());
        assertEquals(1L, event.getTargetId());
        assertEquals("view", event.getActionType());
        assertEquals(180, event.getDuration());

        // 验证JSON格式的detail字段
        String detail = event.getDetail();
        assertTrue(detail.contains("\"action\":\"VIEW_CONCEPT\""));
        assertTrue(detail.contains("\"description\":\"用户查看了概念详情: 1\""));
    }

    // 辅助方法：创建 Concept 对象
    private Concept createConcept(Long id, Integer chapterId, String name, String description) {
        Concept concept = new Concept();
        concept.setId(id.intValue());
        concept.setChapterId(chapterId);
        concept.setName(name);
        concept.setDescription(description);
        return concept;
    }

    // 辅助方法：创建 Question 对象
    private Question createQuestion(Long id, String content, String type) {
        Question question = new Question();
        question.setId(id.intValue());
        question.setContent(content);
        return question;
    }
}