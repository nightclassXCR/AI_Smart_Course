package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.dto.OptionDTO;
import com.dd.ai_smart_course.dto.QuestionDTO;
import com.dd.ai_smart_course.entity.Option;
import com.dd.ai_smart_course.entity.Question;
import com.dd.ai_smart_course.mapper.OptionMapper;
import com.dd.ai_smart_course.mapper.QuestionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionImplTest {

    @Mock
    private QuestionMapper questionMapper;

    @Mock
    private OptionMapper optionMapper;

    @InjectMocks
    private QuestionImpl questionService;

    private QuestionDTO validQuestionDTO;
    private Question validQuestion;
    private List<OptionDTO> validOptionDTOs;
    private List<Option> validOptions;

    @BeforeEach
    void setUp() {
        // 初始化有效的测试数据
        validOptionDTOs = Arrays.asList(
                new OptionDTO("A", "选项A"),
                new OptionDTO("B", "选项B"),
                new OptionDTO("C", "选项C"),
                new OptionDTO("D", "选项D")
        );

        validQuestionDTO = new QuestionDTO();
        validQuestionDTO.setId(1);
        validQuestionDTO.setContent("这是一道测试题目");
        validQuestionDTO.setDifficulty("EASY");
        validQuestionDTO.setPoint(BigDecimal.valueOf(5.0));
        validQuestionDTO.setCourseId(100);
        validQuestionDTO.setAnswer("A");
        validQuestionDTO.setChapterId(10);
        validQuestionDTO.setOptions(validOptionDTOs);

        validQuestion = new Question();
        validQuestion.setId(1);
        validQuestion.setContent("这是一道测试题目");
        validQuestion.setDifficulty(Question.QuestionDifficulty.easy); //
        validQuestion.setPoint(BigDecimal.valueOf(5.0));
        validQuestion.setCourseId(100);
        validQuestion.setAnswer("A");
        validQuestion.setChapterId(10);

        validOptions = Arrays.asList(
                createOption(1, 1, "A", "选项A"),
                createOption(2, 1, "B", "选项B"),
                createOption(3, 1, "C", "选项C"),
                createOption(4, 1, "D", "选项D")
        );
    }

    private Option createOption(int id, int questionId, String key, String value) {
        Option option = new Option();
        option.setId(id);
        option.setQuestionId(questionId);
        option.setOptKey(key);
        option.setOptValue(value);
        return option;
    }

    // =========================== CreateQuestion Tests ===========================


    @Test
    void testCreateQuestion_WithNullQuestionDTO() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> questionService.createQuestion(null));

        assertEquals("QuestionDTO cannot be null", exception.getMessage());
        verify(questionMapper, never()).insert(any(Question.class));
    }


    // =========================== UpdateQuestion Tests ===========================

    @Test
    void testUpdateQuestion_WithNullQuestionDTO() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> questionService.updateQuestion(null));

        assertEquals("QuestionDTO cannot be null", exception.getMessage());
        verify(questionMapper, never()).findById(anyInt());
    }

    // =========================== DeleteQuestion Tests ===========================

    @Test
    void testDeleteQuestion_Success() {
        // Given
        when(questionMapper.findById(1)).thenReturn(validQuestion);
        when(optionMapper.deleteByQuestionId(1)).thenReturn(4);
        when(questionMapper.delete(1)).thenReturn(1);

        // When
        assertDoesNotThrow(() -> questionService.deleteQuestion(1));

        // Then
        verify(questionMapper, times(1)).findById(1);
        verify(optionMapper, times(1)).deleteByQuestionId(1);
        verify(questionMapper, times(1)).delete(1);
    }

    @Test
    void testDeleteQuestion_QuestionNotFound() {
        // Given
        when(questionMapper.findById(999)).thenReturn(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> questionService.deleteQuestion(999));

        assertEquals("Question not found with id: 999", exception.getMessage());
        verify(questionMapper, times(1)).findById(999);
        verify(optionMapper, never()).deleteByQuestionId(anyInt());
        verify(questionMapper, never()).delete(anyInt());
    }

    // =========================== DeleteQuestions Tests ===========================

    @Test
    void testDeleteQuestions_Success() {
        // Given
        List<Integer> ids = Arrays.asList(1, 2, 3);
        when(optionMapper.deleteByQuestionIds(ids)).thenReturn(12);
        when(questionMapper.deleteBatch(ids)).thenReturn(3);

        // When
        assertDoesNotThrow(() -> questionService.deleteQuestions(ids));

        // Then
        verify(optionMapper, times(1)).deleteByQuestionIds(ids);
        verify(questionMapper, times(1)).deleteBatch(ids);
    }

    @Test
    void testDeleteQuestions_WithEmptyList() {
        // Given
        List<Integer> emptyIds = Collections.emptyList();

        // When
        assertDoesNotThrow(() -> questionService.deleteQuestions(emptyIds));

        // Then
        verify(optionMapper, never()).deleteByQuestionIds(anyList());
        verify(questionMapper, never()).deleteBatch(anyList());
    }

    @Test
    void testDeleteQuestions_WithNullList() {
        // When
        assertDoesNotThrow(() -> questionService.deleteQuestions(null));

        // Then
        verify(optionMapper, never()).deleteByQuestionIds(anyList());
        verify(questionMapper, never()).deleteBatch(anyList());
    }

    // =========================== GetQuestion Tests ===========================

    @Test
    void testGetQuestion_Success() {
        // Given
        when(questionMapper.findById(1)).thenReturn(validQuestion);
        when(optionMapper.findByQuestionId(1)).thenReturn(validOptions);

        // When
        QuestionDTO result = questionService.getQuestion(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("这是一道测试题目", result.getContent());
        assertEquals("easy", result.getDifficulty());
        assertEquals(BigDecimal.valueOf(5.0), result.getPoint());
        assertEquals(100, result.getCourseId());
        assertEquals("A", result.getAnswer());
        assertEquals(10, result.getChapterId());
        assertEquals(4, result.getOptions().size());
        assertEquals("A", result.getOptions().get(0).getOptKey());
        assertEquals("选项A", result.getOptions().get(0).getOptValue());

        verify(questionMapper, times(1)).findById(1);
        verify(optionMapper, times(1)).findByQuestionId(1);
    }

    @Test
    void testGetQuestion_QuestionNotFound() {
        // Given
        when(questionMapper.findById(999)).thenReturn(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> questionService.getQuestion(999));

        assertEquals("Question not found with id: 999", exception.getMessage());
        verify(questionMapper, times(1)).findById(999);
        verify(optionMapper, never()).findByQuestionId(anyInt());
    }

    // =========================== ListByCourse Tests ===========================

    @Test
    void testListByCourse_Success() {
        // Given
        List<Question> questions = Arrays.asList(validQuestion);
        when(questionMapper.findByCourseId(100)).thenReturn(questions);
        when(optionMapper.findByQuestionId(1)).thenReturn(validOptions);

        // When
        List<QuestionDTO> result = questionService.listByCourse(100);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        QuestionDTO dto = result.get(0);
        assertEquals(1, dto.getId());
        assertEquals("这是一道测试题目", dto.getContent());
        assertEquals(4, dto.getOptions().size());

        verify(questionMapper, times(1)).findByCourseId(100);
        verify(optionMapper, times(1)).findByQuestionId(1);
    }

    @Test
    void testListByCourse_EmptyResult() {
        // Given
        when(questionMapper.findByCourseId(999)).thenReturn(Collections.emptyList());

        // When
        List<QuestionDTO> result = questionService.listByCourse(999);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(questionMapper, times(1)).findByCourseId(999);
        verify(optionMapper, never()).findByQuestionId(anyInt());
    }

    // =========================== ListByChapter Tests ===========================

    @Test
    void testListByChapter_Success() {
        // Given
        List<Question> questions = Arrays.asList(validQuestion);
        when(questionMapper.findByChapterId(10)).thenReturn(questions);
        when(optionMapper.findByQuestionId(1)).thenReturn(validOptions);

        // When
        List<QuestionDTO> result = questionService.listByChapter(10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        QuestionDTO dto = result.get(0);
        assertEquals(1, dto.getId());
        assertEquals("这是一道测试题目", dto.getContent());
        assertEquals(4, dto.getOptions().size());

        verify(questionMapper, times(1)).findByChapterId(10);
        verify(optionMapper, times(1)).findByQuestionId(1);
    }

    @Test
    void testListByChapter_EmptyResult() {
        // Given
        when(questionMapper.findByChapterId(999)).thenReturn(Collections.emptyList());

        // When
        List<QuestionDTO> result = questionService.listByChapter(999);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(questionMapper, times(1)).findByChapterId(999);
        verify(optionMapper, never()).findByQuestionId(anyInt());
    }

    // =========================== ListById Tests ===========================

    @Test
    void testListById_Success() {
        // Given
        List<Integer> ids = Arrays.asList(1, 2);

        Question question2 = new Question();
        question2.setId(2);
        question2.setContent("第二道题目");
        question2.setDifficulty(Question.QuestionDifficulty.intermediate);
        question2.setPoint(BigDecimal.valueOf(3.0));
        question2.setCourseId(100);
        question2.setAnswer("B");
        question2.setChapterId(10);

        List<Question> questions = Arrays.asList(validQuestion, question2);
        List<Option> options2 = Arrays.asList(
                createOption(5, 2, "A", "选项A2"),
                createOption(6, 2, "B", "选项B2")
        );

        when(questionMapper.findByIds(ids)).thenReturn(questions);
        when(optionMapper.findByQuestionId(1)).thenReturn(validOptions);
        when(optionMapper.findByQuestionId(2)).thenReturn(options2);

        // When
        List<QuestionDTO> result = questionService.listById(ids);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        QuestionDTO dto1 = result.get(0);
        assertEquals(1, dto1.getId());
        assertEquals(4, dto1.getOptions().size());

        QuestionDTO dto2 = result.get(1);
        assertEquals(2, dto2.getId());
        assertEquals(2, dto2.getOptions().size());

        verify(questionMapper, times(1)).findByIds(ids);
        verify(optionMapper, times(1)).findByQuestionId(1);
        verify(optionMapper, times(1)).findByQuestionId(2);
    }

    @Test
    void testListById_EmptyIds() {
        // Given
        List<Integer> emptyIds = Collections.emptyList();

        // When
        List<QuestionDTO> result = questionService.listById(emptyIds);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(questionMapper, never()).findByIds(anyList());
        verify(optionMapper, never()).findByQuestionId(anyInt());
    }

    @Test
    void testListById_NullIds() {
        // When
        List<QuestionDTO> result = questionService.listById(null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(questionMapper, never()).findByIds(anyList());
        verify(optionMapper, never()).findByQuestionId(anyInt());
    }

    // =========================== Exception Tests ===========================

    @Test
    void testGetQuestion_MapperException() {
        // Given
        when(questionMapper.findById(1)).thenThrow(new RuntimeException("查询失败"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> questionService.getQuestion(1));

        assertEquals("查询失败", exception.getMessage());
        verify(questionMapper, times(1)).findById(1);
        verify(optionMapper, never()).findByQuestionId(anyInt());
    }
}