package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.QA;
import com.dd.ai_smart_course.mapper.QAMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QAImplTest {

    @Mock
    private QAMapper qaMapper;

    @InjectMocks
    private QAImpl qaService;

    private QA testQA;

    @BeforeEach
    void setUp() {
        testQA = new QA();
        testQA.setId(1);
        testQA.setUserId(101);
        testQA.setCourseId(202);
        testQA.setConceptId(303);
        testQA.setResponderId(404);
        testQA.setQuestionText("What is polymorphism?");
        testQA.setAnswerText("Polymorphism is the ability of an object to take on many forms.");
        testQA.setResponderType("ai");
        testQA.setStatus("answered");
        testQA.setCreatedAt(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        testQA.setAnsweredAt(Timestamp.valueOf(LocalDateTime.now()));
        testQA.setRating(true);
    }

    @Test
    void testGetAllQA_Success() {
        // Given
        List<QA> expectedQAs = Arrays.asList(testQA, createAnotherQA());
        when(qaMapper.getAllQAs()).thenReturn(expectedQAs);

        // When
        List<QA> actualQAs = qaService.getAllQA();

        // Then
        assertNotNull(actualQAs);
        assertEquals(2, actualQAs.size());
        assertEquals(expectedQAs, actualQAs);
        verify(qaMapper, times(1)).getAllQAs();
    }

    @Test
    void testGetAllQA_EmptyList() {
        // Given
        when(qaMapper.getAllQAs()).thenReturn(Collections.emptyList());

        // When
        List<QA> actualQAs = qaService.getAllQA();

        // Then
        assertNotNull(actualQAs);
        assertTrue(actualQAs.isEmpty());
        verify(qaMapper, times(1)).getAllQAs();
    }

    @Test
    void testGetQAById_Success() {
        // Given
        int qaId = 1;
        when(qaMapper.getQAById(qaId)).thenReturn(testQA);

        // When
        QA actualQA = qaService.getQAById(qaId);

        // Then
        assertNotNull(actualQA);
        assertEquals(testQA.getId(), actualQA.getId());
        assertEquals(testQA.getQuestionText(), actualQA.getQuestionText());
        verify(qaMapper, times(1)).getQAById(qaId);
    }

    @Test
    void testGetQAById_NotFound() {
        // Given
        int qaId = 999;
        when(qaMapper.getQAById(qaId)).thenReturn(null);

        // When
        QA actualQA = qaService.getQAById(qaId);

        // Then
        assertNull(actualQA);
        verify(qaMapper, times(1)).getQAById(qaId);
    }

    @Test
    void testAddLog_Success() {
        // Given
        int expectedResult = 1;
        when(qaMapper.addQA(testQA)).thenReturn(expectedResult);

        // When
        int actualResult = qaService.addLog(testQA);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(qaMapper, times(1)).addQA(testQA);
    }

    @Test
    void testAddLog_Failed() {
        // Given
        int expectedResult = 0;
        when(qaMapper.addQA(testQA)).thenReturn(expectedResult);

        // When
        int actualResult = qaService.addLog(testQA);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(qaMapper, times(1)).addQA(testQA);
    }

    @Test
    void testUpdateQA_Success() {
        // Given
        int expectedResult = 1;
        when(qaMapper.updateQA(testQA)).thenReturn(expectedResult);

        // When
        int actualResult = qaService.updateQA(testQA);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(qaMapper, times(1)).updateQA(testQA);
    }

    @Test
    void testUpdateQA_NotFound() {
        // Given
        int expectedResult = 0;
        when(qaMapper.updateQA(testQA)).thenReturn(expectedResult);

        // When
        int actualResult = qaService.updateQA(testQA);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(qaMapper, times(1)).updateQA(testQA);
    }

    @Test
    void testDeleteQA_Success() {
        // Given
        int qaId = 1;
        int expectedResult = 1;
        when(qaMapper.deleteQA(qaId)).thenReturn(expectedResult);

        // When
        int actualResult = qaService.deleteQA(qaId);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(qaMapper, times(1)).deleteQA(qaId);
    }

    @Test
    void testDeleteQA_NotFound() {
        // Given
        int qaId = 999;
        int expectedResult = 0;
        when(qaMapper.deleteQA(qaId)).thenReturn(expectedResult);

        // When
        int actualResult = qaService.deleteQA(qaId);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(qaMapper, times(1)).deleteQA(qaId);
    }

    // Helper method to create another test QA object
    private QA createAnotherQA() {
        QA anotherQA = new QA();
        anotherQA.setId(2);
        anotherQA.setUserId(102);
        anotherQA.setCourseId(203);
        anotherQA.setConceptId(304);
        anotherQA.setResponderId(405);
        anotherQA.setQuestionText("What is inheritance?");
        anotherQA.setAnswerText("Inheritance is a mechanism wherein a new class is derived from an existing class.");
        anotherQA.setResponderType("teacher");
        anotherQA.setStatus("answered");
        anotherQA.setCreatedAt(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
        anotherQA.setAnsweredAt(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        anotherQA.setRating(false);
        return anotherQA;
    }
}