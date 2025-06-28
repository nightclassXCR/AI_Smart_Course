//package com.dd.ai_smart_course.service.impl;
//
//import com.dd.ai_smart_course.entity.Question;
//import com.dd.ai_smart_course.entity.Score;
//import com.dd.ai_smart_course.entity.Task;
//import com.dd.ai_smart_course.event.LearningActionEvent;
//import com.dd.ai_smart_course.mapper.QuestionMapper;
//import com.dd.ai_smart_course.mapper.ScoreMapper;
//import com.dd.ai_smart_course.mapper.TaskMapper;
//import com.dd.ai_smart_course.service.base.ConceptMasteryService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.ApplicationEventPublisher;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TaskServiceImplTest {
//
//    @Mock
//    private TaskMapper taskMapper;
//
//    @Mock
//    private ScoreMapper scoreMapper;
//
//    @Mock
//    private QuestionMapper questionMapper;
//
//    @Mock
//    private ApplicationEventPublisher eventPublisher;
//
//    @Mock
//    private ConceptMasteryService conceptMasteryService;
//
//    @InjectMocks
//    private TaskServiceImpl taskService;
//
//    private Task testTask;
//    private Score testScore;
//    private Question testQuestion;
//
//    @BeforeEach
//    void setUp() {
//        testTask = new Task();
//        testTask.setId(1);
//        testTask.setCourseId(100);
//        testTask.setTitle("Test Task");
//        testTask.setType(Task.Type.quiz);
//        testTask.setStatus(Task.Status.published);
//        testTask.setCreatedAt(LocalDateTime.now());
//        testTask.setUpdatedAt(LocalDateTime.now());
//
//        testScore = new Score();
//        testScore.setId(1);
//        testScore.setUserId(1);
//        testScore.setTaskId(1);
//        testScore.setTotalScore(new BigDecimal("85.5"));
//
//        testQuestion = new Question();
//        testQuestion.setId(1);
//        // 假设 Question 有 taskId 字段
//    }
//
//    @Test
//    void testInsertBatch() {
//        // Given
//        List<Task> tasks = Arrays.asList(testTask);
//
//        // When
//        taskService.insertBatch(tasks);
//
//        // Then
//        verify(taskMapper, times(1)).insertBatch(tasks);
//    }
//
//    @Test
//    void testInsertBatch_EmptyList() {
//        // Given
//        List<Task> emptyTasks = Collections.emptyList();
//
//        // When
//        taskService.insertBatch(emptyTasks);
//
//        // Then
//        verify(taskMapper, times(1)).insertBatch(emptyTasks);
//    }
//
//    @Test
//    void testListByCourseId() {
//        // Given
//        int courseId = 100;
//        List<Task> expectedTasks = Arrays.asList(testTask);
//        when(taskMapper.listByCourseId(courseId)).thenReturn(expectedTasks);
//
//        // When
//        List<Task> actualTasks = taskService.listByCourseId(courseId);
//
//        // Then
//        assertEquals(expectedTasks, actualTasks);
//        verify(taskMapper, times(1)).listByCourseId(courseId);
//    }
//
//    @Test
//    void testListByCourseId_EmptyResult() {
//        // Given
//        int courseId = 999;
//        when(taskMapper.listByCourseId(courseId)).thenReturn(Collections.emptyList());
//
//        // When
//        List<Task> actualTasks = taskService.listByCourseId(courseId);
//
//        // Then
//        assertTrue(actualTasks.isEmpty());
//        verify(taskMapper, times(1)).listByCourseId(courseId);
//    }
//
//    @Test
//    void testDelete() {
//        // Given
//        int taskId = 1;
//        List<Score> scores = Arrays.asList(testScore);
//        List<Question> questions = Arrays.asList(testQuestion);
//
//        when(scoreMapper.listByTaskId(taskId)).thenReturn(scores);
//        when(questionMapper.findByTaskId(taskId)).thenReturn(questions);
//
//        // When
//        taskService.delete(taskId);
//
//        // Then
//        verify(scoreMapper, times(1)).listByTaskId(taskId);
//        verify(scoreMapper, times(1)).deleteById(testScore.getId());
//        verify(questionMapper, times(1)).findByTaskId(taskId);
//        verify(questionMapper, times(1)).deleteBatch(anyList());
//        verify(taskMapper, times(1)).deleteByTaskId(taskId);
//    }
//
//    @Test
//    void testDelete_NoScoresOrQuestions() {
//        // Given
//        int taskId = 1;
//        when(scoreMapper.listByTaskId(taskId)).thenReturn(Collections.emptyList());
//        when(questionMapper.findByTaskId(taskId)).thenReturn(Collections.emptyList());
//
//        // When
//        taskService.delete(taskId);
//
//        // Then
//        verify(scoreMapper, times(1)).listByTaskId(taskId);
//        verify(scoreMapper, never()).deleteById(anyInt());
//        verify(questionMapper, times(1)).findByTaskId(taskId);
//        verify(questionMapper, times(1)).deleteBatch(Collections.emptyList());
//        verify(taskMapper, times(1)).deleteByTaskId(taskId);
//    }
//
//    @Test
//    void testUpdate() {
//        // Given
//        Task taskToUpdate = new Task();
//        taskToUpdate.setId(1);
//        taskToUpdate.setTitle("Updated Task");
//
//        // When
//        taskService.update(taskToUpdate);
//
//        // Then
//        assertNotNull(taskToUpdate.getUpdatedAt());
//        verify(taskMapper, times(1)).update(taskToUpdate);
//    }
//
//    @Test
//    void testStartTask_Success() {
//        // Given
//        Long taskId = 1L;
//        Long userId = 100L;
//        when(taskMapper.findById(1)).thenReturn(Optional.of(testTask));
//
//        // When
//        taskService.startTask(taskId, userId);
//
//        // Then
//        verify(taskMapper, times(1)).findById(1);
//
//        ArgumentCaptor<LearningActionEvent> eventCaptor = ArgumentCaptor.forClass(LearningActionEvent.class);
//        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
//
//        LearningActionEvent capturedEvent = eventCaptor.getValue();
//        assertEquals(100, capturedEvent.getUserId());
//        assertEquals("task", capturedEvent.getTargetType());
//        assertEquals(1L, capturedEvent.getTargetId());
//        assertEquals("click", capturedEvent.getActionType());
//        assertTrue(capturedEvent.getDetail().contains("START_TASK"));
//    }
//
//    @Test
//    void testStartTask_TaskNotFound() {
//        // Given
//        Long taskId = 999L;
//        Long userId = 100L;
//        when(taskMapper.findById(999)).thenReturn(Optional.empty());
//
//        // When & Then
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> taskService.startTask(taskId, userId)
//        );
//
//        assertEquals("Task not found: 999", exception.getMessage());
//        verify(taskMapper, times(1)).findById(999);
//        verify(eventPublisher, never()).publishEvent(any());
//    }
//
//    @Test
//    void testSubmitTask_Success() {
//        // Given
//        int taskId = 1;
//        int userId = 100;
//        BigDecimal rawScore = new BigDecimal("85.5");
//        String submissionContent = "Test submission";
//
//        when(taskMapper.findById(taskId)).thenReturn(Optional.of(testTask));
//
//        // When
//        Score result = taskService.submitTask(taskId, userId, rawScore, submissionContent);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(userId, result.getUserId());
//        assertEquals(taskId, result.getTaskId());
//        assertEquals(rawScore, result.getTotalScore());
//
//        verify(taskMapper, times(1)).findById(taskId);
//        verify(scoreMapper, times(1)).insertBatch(anyList());
//        verify(conceptMasteryService, times(1))
//                .updateMasteryForTaskSubmission(100L, 1L);
//
//        ArgumentCaptor<LearningActionEvent> eventCaptor = ArgumentCaptor.forClass(LearningActionEvent.class);
//        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
//
//        LearningActionEvent capturedEvent = eventCaptor.getValue();
//        assertEquals(100, capturedEvent.getUserId());
//        assertEquals("task", capturedEvent.getTargetType());
//        assertEquals(1L, capturedEvent.getTargetId());
//        assertEquals("submit", capturedEvent.getActionType());
//        assertTrue(capturedEvent.getDetail().contains("85.5"));
//    }
//
//    @Test
//    void testSubmitTask_TaskNotFound() {
//        // Given
//        int taskId = 999;
//        int userId = 100;
//        BigDecimal rawScore = new BigDecimal("85.5");
//        String submissionContent = "Test submission";
//
//        when(taskMapper.findById(taskId)).thenReturn(Optional.empty());
//
//        // When & Then
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> taskService.submitTask(taskId, userId, rawScore, submissionContent)
//        );
//
//        assertEquals("Task not found: 999", exception.getMessage());
//        verify(scoreMapper, never()).insertBatch(anyList());
//        verify(eventPublisher, never()).publishEvent(any());
//        verify(conceptMasteryService, never()).updateMasteryForTaskSubmission(anyLong(), anyLong());
//    }
//
//    @Test
//    void testAnswerQuestion_Success() {
//        // Given
//        Long questionId = 1L;
//        Long userId = 100L;
//        String userAnswer = "Answer A";
//        boolean isCorrect = true;
//
//        when(questionMapper.findById(1)).thenReturn(testQuestion);
//
//        // When
//        taskService.answerQuestion(questionId, userId, userAnswer, isCorrect);
//
//        // Then
//        verify(questionMapper, times(1)).findById(1);
//
//        ArgumentCaptor<LearningActionEvent> eventCaptor = ArgumentCaptor.forClass(LearningActionEvent.class);
//        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
//
//        LearningActionEvent capturedEvent = eventCaptor.getValue();
//        assertEquals(100, capturedEvent.getUserId());
//        assertEquals("QUESTION", capturedEvent.getTargetType());
//        assertEquals(1L, capturedEvent.getTargetId());
//        assertEquals("answer", capturedEvent.getActionType());
//        assertTrue(capturedEvent.getDetail().contains("Answer A"));
//        assertTrue(capturedEvent.getDetail().contains("true"));
//    }
//
//    @Test
//    void testAnswerQuestion_IncorrectAnswer() {
//        // Given
//        Long questionId = 1L;
//        Long userId = 100L;
//        String userAnswer = "Wrong Answer";
//        boolean isCorrect = false;
//
//        when(questionMapper.findById(1)).thenReturn(testQuestion);
//
//        // When
//        taskService.answerQuestion(questionId, userId, userAnswer, isCorrect);
//
//        // Then
//        verify(questionMapper, times(1)).findById(1);
//
//        ArgumentCaptor<LearningActionEvent> eventCaptor = ArgumentCaptor.forClass(LearningActionEvent.class);
//        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
//
//        LearningActionEvent capturedEvent = eventCaptor.getValue();
//        assertTrue(capturedEvent.getDetail().contains("false"));
//    }
//
//    @Test
//    void testAnswerQuestion_QuestionNotFound() {
//        // Given
//        Long questionId = 999L;
//        Long userId = 100L;
//        String userAnswer = "Answer A";
//        boolean isCorrect = true;
//
//        when(questionMapper.findById(999)).thenReturn(null);
//
//        // When & Then
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> taskService.answerQuestion(questionId, userId, userAnswer, isCorrect)
//        );
//
//        assertEquals("Question not found: 999", exception.getMessage());
//        verify(questionMapper, times(1)).findById(999);
//        verify(eventPublisher, never()).publishEvent(any());
//    }
//
//    @Test
//    void testSubmitTask_WithNullSubmissionContent() {
//        // Given
//        int taskId = 1;
//        int userId = 100;
//        BigDecimal rawScore = new BigDecimal("90.0");
//        String submissionContent = null;
//
//        when(taskMapper.findById(taskId)).thenReturn(Optional.of(testTask));
//
//        // When
//        Score result = taskService.submitTask(taskId, userId, rawScore, submissionContent);
//
//        // Then
//        assertNotNull(result);
//        verify(scoreMapper, times(1)).insertBatch(anyList());
//
//        ArgumentCaptor<LearningActionEvent> eventCaptor = ArgumentCaptor.forClass(LearningActionEvent.class);
//        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
//
//        LearningActionEvent capturedEvent = eventCaptor.getValue();
//        assertTrue(capturedEvent.getDetail().contains("null"));
//    }
//
//    @Test
//    void testAnswerQuestion_WithSpecialCharacters() {
//        // Given
//        Long questionId = 1L;
//        Long userId = 100L;
//        String userAnswer = "Answer with \"quotes\" and \\backslashes";
//        boolean isCorrect = true;
//
//        when(questionMapper.findById(1)).thenReturn(testQuestion);
//
//        // When
//        taskService.answerQuestion(questionId, userId, userAnswer, isCorrect);
//
//        // Then
//        verify(questionMapper, times(1)).findById(1);
//        verify(eventPublisher, times(1)).publishEvent(any(LearningActionEvent.class));
//    }
//}