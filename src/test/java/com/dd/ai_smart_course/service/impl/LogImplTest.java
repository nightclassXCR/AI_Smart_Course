package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.LearningLog;
import com.dd.ai_smart_course.mapper.LogMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogImplTest {

    @Mock
    private LogMapper logMapper;

    @InjectMocks
    private LogImpl logService;

    private LearningLog testLog;
    private LocalDateTime testStartDate;
    private LocalDateTime testEndDate;

    @BeforeEach
    void setUp() {
        testLog = new LearningLog();
        testLog.setId(1);
        testLog.setUserId(100);
        testLog.setTargetType("task");
        testLog.setTargetId(1);
        testLog.setActionType("click");
        testLog.setDetail("Test log detail");

        testStartDate = LocalDateTime.of(2025, 1, 1, 0, 0);
        testEndDate = LocalDateTime.of(2025, 12, 31, 23, 59);
    }

    @Test
    void testGetAllLogs_Success() {
        // Given
        List<LearningLog> expectedLogs = Arrays.asList(testLog);
        when(logMapper.getAllLogs()).thenReturn(expectedLogs);

        // When
        List<LearningLog> actualLogs = logService.getAllLogs();

        // Then
        assertNotNull(actualLogs);
        assertEquals(1, actualLogs.size());
        assertEquals(expectedLogs, actualLogs);
        verify(logMapper, times(1)).getAllLogs();
    }

    @Test
    void testGetAllLogs_EmptyResult() {
        // Given
        when(logMapper.getAllLogs()).thenReturn(Collections.emptyList());

        // When
        List<LearningLog> actualLogs = logService.getAllLogs();

        // Then
        assertNotNull(actualLogs);
        assertTrue(actualLogs.isEmpty());
        verify(logMapper, times(1)).getAllLogs();
    }

    @Test
    void testGetAllLogs_NullResult() {
        // Given
        when(logMapper.getAllLogs()).thenReturn(null);

        // When
        List<LearningLog> actualLogs = logService.getAllLogs();

        // Then
        assertNull(actualLogs);
        verify(logMapper, times(1)).getAllLogs();
    }

    @Test
    void testGetLogById_Success() {
        // Given
        int logId = 1;
        when(logMapper.getLogById(logId)).thenReturn(testLog);

        // When
        LearningLog actualLog = logService.getLogById(logId);

        // Then
        assertNotNull(actualLog);
        assertEquals(testLog, actualLog);
        assertEquals(1, actualLog.getId());
        assertEquals(100L, actualLog.getUserId());
        verify(logMapper, times(1)).getLogById(logId);
    }

    @Test
    void testGetLogById_NotFound() {
        // Given
        int logId = 999;
        when(logMapper.getLogById(logId)).thenReturn(null);

        // When
        LearningLog actualLog = logService.getLogById(logId);

        // Then
        assertNull(actualLog);
        verify(logMapper, times(1)).getLogById(logId);
    }

    @Test
    void testGetLogById_WithZeroId() {
        // Given
        int logId = 0;
        when(logMapper.getLogById(logId)).thenReturn(null);

        // When
        LearningLog actualLog = logService.getLogById(logId);

        // Then
        assertNull(actualLog);
        verify(logMapper, times(1)).getLogById(logId);
    }


    @Test
    void testDeleteLog_Success() {
        // Given
        int logId = 1;
        int expectedResult = 1; // 成功删除返回1
        when(logMapper.deleteLog(logId)).thenReturn(expectedResult);

        // When
        int actualResult = logService.deleteLog(logId);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(logMapper, times(1)).deleteLog(logId);
    }

    @Test
    void testDeleteLog_NotFound() {
        // Given
        int logId = 999;
        int expectedResult = 0; // 删除失败返回0
        when(logMapper.deleteLog(logId)).thenReturn(expectedResult);

        // When
        int actualResult = logService.deleteLog(logId);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(logMapper, times(1)).deleteLog(logId);
    }

    @Test
    void testDeleteLog_WithNegativeId() {
        // Given
        int logId = -1;
        int expectedResult = 0;
        when(logMapper.deleteLog(logId)).thenReturn(expectedResult);

        // When
        int actualResult = logService.deleteLog(logId);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(logMapper, times(1)).deleteLog(logId);
    }

    @Test
    void testFindLearningLogs_WithAllParameters() {
        // Given
        int userId = 100;
        String targetType = "task";
        String actionType = "click";
        Integer offset = 0;
        Integer limit = 10;
        List<LearningLog> expectedLogs = Arrays.asList(testLog);

        when(logMapper.findLearningLogs(userId, targetType, actionType, testStartDate, testEndDate, offset, limit))
                .thenReturn(expectedLogs);

        // When
        List<LearningLog> actualLogs = logService.findLearningLogs(
                userId, targetType, actionType, testStartDate, testEndDate, offset, limit);

        // Then
        assertNotNull(actualLogs);
        assertEquals(1, actualLogs.size());
        assertEquals(expectedLogs, actualLogs);
        verify(logMapper, times(1)).findLearningLogs(
                userId, targetType, actionType, testStartDate, testEndDate, offset, limit);
    }

    @Test
    void testFindLearningLogs_WithPagination() {
        // Given
        int userId = 100;
        Integer offset = 20;
        Integer limit = 5;
        List<LearningLog> expectedLogs = Arrays.asList(testLog);

        when(logMapper.findLearningLogs(userId, null, null, null, null, offset, limit))
                .thenReturn(expectedLogs);

        // When
        List<LearningLog> actualLogs = logService.findLearningLogs(
                userId, null, null, null, null, offset, limit);

        // Then
        assertNotNull(actualLogs);
        assertEquals(1, actualLogs.size());
        verify(logMapper, times(1)).findLearningLogs(
                userId, null, null, null, null, offset, limit);
    }

    @Test
    void testCountLearningLogs_WithAllParameters() {
        // Given
        int userId = 100;
        String targetType = "task";
        String actionType = "click";
        int expectedCount = 5;

        when(logMapper.countLearningLogs(userId, targetType, actionType, testStartDate, testEndDate))
                .thenReturn(expectedCount);

        // When
        int actualCount = logService.countLearningLogs(
                userId, targetType, actionType, testStartDate, testEndDate);

        // Then
        assertEquals(expectedCount, actualCount);
        verify(logMapper, times(1)).countLearningLogs(
                userId, targetType, actionType, testStartDate, testEndDate);
    }



    @Test
    void testCountLearningLogs_ZeroResult() {
        // Given
        int userId = 999;
        int expectedCount = 0;
        when(logMapper.countLearningLogs(eq(userId), any(), any(), any(), any()))
                .thenReturn(expectedCount);

        // When
        int actualCount = logService.countLearningLogs(
                userId, "task", "click", testStartDate, testEndDate);

        // Then
        assertEquals(expectedCount, actualCount);
        verify(logMapper, times(1)).countLearningLogs(
                userId, "task", "click", testStartDate, testEndDate);
    }


    @Test
    void testFindLearningLogs_DifferentTargetTypes() {
        // Given
        int userId = 100;
        String[] targetTypes = {"task", "question", "course", "lesson"};

        for (String targetType : targetTypes) {
            List<LearningLog> expectedLogs = Arrays.asList(testLog);
            when(logMapper.findLearningLogs(userId, targetType, null, null, null, null, null))
                    .thenReturn(expectedLogs);

            // When
            List<LearningLog> actualLogs = logService.findLearningLogs(
                    userId, targetType, null, null, null, null, null);

            // Then
            assertNotNull(actualLogs);
            assertEquals(1, actualLogs.size());
        }

        verify(logMapper, times(targetTypes.length)).findLearningLogs(
                eq(userId), any(), isNull(), isNull(), isNull(), isNull(), isNull());
    }

    @Test
    void testFindLearningLogs_DifferentActionTypes() {
        // Given
        int userId = 100;
        String[] actionTypes = {"click", "submit", "answer", "view"};

        for (String actionType : actionTypes) {
            List<LearningLog> expectedLogs = Arrays.asList(testLog);
            when(logMapper.findLearningLogs(userId, null, actionType, null, null, null, null))
                    .thenReturn(expectedLogs);

            // When
            List<LearningLog> actualLogs = logService.findLearningLogs(
                    userId, null, actionType, null, null, null, null);

            // Then
            assertNotNull(actualLogs);
            assertEquals(1, actualLogs.size());
        }

        verify(logMapper, times(actionTypes.length)).findLearningLogs(
                eq(userId), isNull(), any(), isNull(), isNull(), isNull(), isNull());
    }
}