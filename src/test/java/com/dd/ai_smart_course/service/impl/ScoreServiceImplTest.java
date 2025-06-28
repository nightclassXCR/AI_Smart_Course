//package com.dd.ai_smart_course.service.impl;
//
//import com.dd.ai_smart_course.entity.Score;
//import com.dd.ai_smart_course.entity.Score.GradingMethod;
//import com.dd.ai_smart_course.mapper.ScoreMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ScoreServiceImplTest {
//
//    @Mock
//    private ScoreMapper scoreMapper;
//
//    @InjectMocks
//    private ScoreServiceImpl scoreService;
//
//    private Score testScore;
//    private Score anotherScore;
//
//    @BeforeEach
//    void setUp() {
//        testScore = new Score();
//        testScore.setId(1);
//        testScore.setUserId(101);
//        testScore.setTaskId(201);
//        testScore.setTotalScore(BigDecimal.valueOf(95.5));
//        testScore.setAiScore(BigDecimal.valueOf(90.0));
//        testScore.setTeacherScore(BigDecimal.valueOf(95.5));
//        testScore.setFinalScore(BigDecimal.valueOf(95.5));
//        testScore.setComment("Excellent work!");
//        testScore.setSubmittedAt(LocalDateTime.now().minusHours(5));
//        testScore.setGradedAt(LocalDateTime.now().minusHours(2));
//        testScore.setGradingMethod(GradingMethod.mixed);
//
//        anotherScore = new Score();
//        anotherScore.setId(2);
//        anotherScore.setUserId(102);
//        anotherScore.setTaskId(202);
//        anotherScore.setTotalScore(BigDecimal.valueOf(88.0));
//        anotherScore.setAiScore(BigDecimal.valueOf(85.0));
//        anotherScore.setTeacherScore(BigDecimal.valueOf(88.0));
//        anotherScore.setFinalScore(BigDecimal.valueOf(88.0));
//        anotherScore.setComment("Good effort.");
//        anotherScore.setSubmittedAt(LocalDateTime.now().minusDays(1));
//        anotherScore.setGradedAt(LocalDateTime.now().minusHours(1));
//        anotherScore.setGradingMethod(GradingMethod.teacher);
//    }
//
//    @Test
//    void testGetById_Success() {
//        // Given
//        int scoreId = 1;
//        when(scoreMapper.getById(scoreId)).thenReturn(testScore);
//
//        // When
//        Score actualScore = scoreService.getById(scoreId);
//
//        // Then
//        assertNotNull(actualScore);
//        assertEquals(testScore.getId(), actualScore.getId());
//        assertEquals(testScore.getUserId(), actualScore.getUserId());
//        verify(scoreMapper, times(1)).getById(scoreId);
//    }
//
//    @Test
//    void testGetById_NotFound() {
//        // Given
//        int scoreId = 999;
//        when(scoreMapper.getById(scoreId)).thenReturn(null);
//
//        // When
//        Score actualScore = scoreService.getById(scoreId);
//
//        // Then
//        assertNull(actualScore);
//        verify(scoreMapper, times(1)).getById(scoreId);
//    }
//
//    @Test
//    void testInsertBatch_Success() {
//        // Given
//        List<Score> scoresToInsert = Arrays.asList(testScore, anotherScore);
//
//        // When
//        scoreService.insertBatch(scoresToInsert);
//
//        // Then
//        verify(scoreMapper, times(1)).insertBatch(scoresToInsert);
//    }
//
//    @Test
//    void testInsertBatch_EmptyList() {
//        // Given
//        List<Score> scoresToInsert = Collections.emptyList();
//
//        // When
//        scoreService.insertBatch(scoresToInsert);
//
//        // Then
//        verify(scoreMapper, times(1)).insertBatch(scoresToInsert); // Mapper should still be called even with empty list
//    }
//
//    @Test
//    void testDeleteBatch_Success() {
//        // Given
//        List<Integer> idsToDelete = Arrays.asList(1, 2);
//
//        // When
//        scoreService.deleteBatch(idsToDelete);
//
//        // Then
//        verify(scoreMapper, times(1)).deleteByIds(idsToDelete);
//    }
//
//    @Test
//    void testDeleteBatch_EmptyList() {
//        // Given
//        List<Integer> idsToDelete = Collections.emptyList();
//
//        // When
//        scoreService.deleteBatch(idsToDelete);
//
//        // Then
//        verify(scoreMapper, times(1)).deleteByIds(idsToDelete);
//    }
//
//    @Test
//    void testUpdate_Success() {
//        // Given
//        testScore.setComment("Updated comment.");
//        testScore.setGradedAt(null); // Simulate a fresh update call where gradedAt is set by service
//
//        // When
//        scoreService.update(testScore);
//
//        // Then
//        ArgumentCaptor<Score> scoreCaptor = ArgumentCaptor.forClass(Score.class);
//        verify(scoreMapper, times(1)).update(scoreCaptor.capture());
//
//        Score capturedScore = scoreCaptor.getValue();
//        assertNotNull(capturedScore.getGradedAt()); // Should be set by the service
//        assertEquals("Updated comment.", capturedScore.getComment());
//        assertEquals(testScore.getId(), capturedScore.getId()); // Ensure other fields are unchanged
//    }
//
//    @Test
//    void testGetTaskScore_Success_WithResults() {
//        // Given
//        int taskId = 201;
//        when(scoreMapper.listByTaskId(taskId)).thenReturn(Arrays.asList(testScore, createScoreForTaskId(taskId)));
//
//        // When
//        Score actualScore = scoreService.getTaskScore(taskId);
//
//        // Then
//        assertNotNull(actualScore);
//        assertEquals(testScore.getId(), actualScore.getId()); // Should return the first one
//        verify(scoreMapper, times(1)).listByTaskId(taskId);
//    }
//
//    @Test
//    void testGetTaskScore_NoResults() {
//        // Given
//        int taskId = 999;
//        when(scoreMapper.listByTaskId(taskId)).thenReturn(Collections.emptyList());
//
//        // When
//        Score actualScore = scoreService.getTaskScore(taskId);
//
//        // Then
//        assertNull(actualScore);
//        verify(scoreMapper, times(1)).listByTaskId(taskId);
//    }
//
//    @Test
//    void testGetUserScores_Success_WithResults() {
//        // Given
//        int userId = 101;
//        List<Score> expectedScores = Arrays.asList(testScore, createScoreForUserId(userId));
//        when(scoreMapper.getScoreByUserId(userId)).thenReturn(expectedScores);
//
//        // When
//        List<Score> actualScores = scoreService.getUserScores(userId);
//
//        // Then
//        assertNotNull(actualScores);
//        assertEquals(2, actualScores.size());
//        assertTrue(actualScores.contains(testScore));
//        assertTrue(actualScores.contains(createScoreForUserId(userId)));
//        verify(scoreMapper, times(1)).getScoreByUserId(userId);
//    }
//
//    @Test
//    void testGetUserScores_NoResults() {
//        // Given
//        int userId = 999;
//        when(scoreMapper.getScoreByUserId(userId)).thenReturn(Collections.emptyList());
//
//        // When
//        List<Score> actualScores = scoreService.getUserScores(userId);
//
//        // Then
//        assertNotNull(actualScores);
//        assertTrue(actualScores.isEmpty());
//        verify(scoreMapper, times(1)).getScoreByUserId(userId);
//    }
//
//    // Helper method to create a score for a specific taskId
//    private Score createScoreForTaskId(int taskId) {
//        Score score = new Score();
//        score.setId(3);
//        score.setUserId(101); // Same user, different task or same task different attempt
//        score.setTaskId(taskId);
//        score.setTotalScore(BigDecimal.valueOf(92.0));
//        score.setAiScore(BigDecimal.valueOf(91.0));
//        score.setTeacherScore(BigDecimal.valueOf(92.0));
//        score.setFinalScore(BigDecimal.valueOf(92.0));
//        score.setComment("Another attempt score.");
//        score.setSubmittedAt(LocalDateTime.now().minusHours(4));
//        score.setGradedAt(LocalDateTime.now().minusHours(1));
//        score.setGradingMethod(GradingMethod.ai);
//        return score;
//    }
//
//    // Helper method to create a score for a specific userId
//    private Score createScoreForUserId(int userId) {
//        Score score = new Score();
//        score.setId(4);
//        score.setUserId(userId);
//        score.setTaskId(203); // Different task
//        score.setTotalScore(BigDecimal.valueOf(78.0));
//        score.setAiScore(BigDecimal.valueOf(75.0));
//        score.setTeacherScore(BigDecimal.valueOf(78.0));
//        score.setFinalScore(BigDecimal.valueOf(78.0));
//        score.setComment("Another score for this user.");
//        score.setSubmittedAt(LocalDateTime.now().minusDays(2));
//        score.setGradedAt(LocalDateTime.now().minusDays(1));
//        score.setGradingMethod(GradingMethod.mixed);
//        return score;
//    }
//}