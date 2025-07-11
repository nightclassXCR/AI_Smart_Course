package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.File;
import com.dd.ai_smart_course.event.LearningActionEvent;
import com.dd.ai_smart_course.mapper.FileMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileImplTest {

    @Mock
    private FileMapper fileMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private FileImpl fileService;

    private File testFile;

    @BeforeEach
    void setUp() {
        testFile = new File();
        testFile.setId(1);
        testFile.setName("test-file.pdf");
        testFile.setFileUrl("/files/test-file.pdf");
        testFile.setFileType("PDF");
        testFile.setOwnerType("COURSE");
        testFile.setOwnerId(100);
        testFile.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        testFile.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Test
    void testGetAllFiles_Success() {
        // Given
        List<File> expectedFiles = Arrays.asList(testFile, createAnotherFile());
        when(fileMapper.getAllFiles()).thenReturn(expectedFiles);

        // When
        List<File> actualFiles = fileService.getAllFiles();

        // Then
        assertNotNull(actualFiles);
        assertEquals(2, actualFiles.size());
        assertEquals(expectedFiles, actualFiles);
        verify(fileMapper, times(1)).getAllFiles();
    }

    @Test
    void testGetAllFiles_EmptyList() {
        // Given
        when(fileMapper.getAllFiles()).thenReturn(Arrays.asList());

        // When
        List<File> actualFiles = fileService.getAllFiles();

        // Then
        assertNotNull(actualFiles);
        assertTrue(actualFiles.isEmpty());
        verify(fileMapper, times(1)).getAllFiles();
    }

    @Test
    void testGetFileById_Success() {
        // Given
        int fileId = 1;
        when(fileMapper.getFileById(fileId)).thenReturn(testFile);

        // When
        File actualFile = fileService.getFileById(fileId);

        // Then
        assertNotNull(actualFile);
        assertEquals(testFile.getId(), actualFile.getId());
        assertEquals(testFile.getName(), actualFile.getName());
        verify(fileMapper, times(1)).getFileById(fileId);
    }

    @Test
    void testGetFileById_NotFound() {
        // Given
        int fileId = 999;
        when(fileMapper.getFileById(fileId)).thenReturn(null);

        // When
        File actualFile = fileService.getFileById(fileId);

        // Then
        assertNull(actualFile);
        verify(fileMapper, times(1)).getFileById(fileId);
    }

    @Test
    void testAddFile_Success() {
        // Given
        int expectedResult = 1;
        when(fileMapper.addFile(testFile)).thenReturn(expectedResult);

        // When
        int actualResult = fileService.addFile(testFile);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(fileMapper, times(1)).addFile(testFile);
    }

    @Test
    void testAddFile_Failed() {
        // Given
        int expectedResult = 0;
        when(fileMapper.addFile(testFile)).thenReturn(expectedResult);

        // When
        int actualResult = fileService.addFile(testFile);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(fileMapper, times(1)).addFile(testFile);
    }

    @Test
    void testUpdateFile_Success() {
        // Given
        int expectedResult = 1;
        when(fileMapper.updateFile(testFile)).thenReturn(expectedResult);

        // When
        int actualResult = fileService.updateFile(testFile);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(fileMapper, times(1)).updateFile(testFile);
    }

    @Test
    void testUpdateFile_NotFound() {
        // Given
        int expectedResult = 0;
        when(fileMapper.updateFile(testFile)).thenReturn(expectedResult);

        // When
        int actualResult = fileService.updateFile(testFile);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(fileMapper, times(1)).updateFile(testFile);
    }

    @Test
    void testDeleteFile_Success() {
        // Given
        int fileId = 1;
        int expectedResult = 1;
        when(fileMapper.deleteFile(fileId)).thenReturn(expectedResult);

        // When
        int actualResult = fileService.deleteFile(fileId);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(fileMapper, times(1)).deleteFile(fileId);
    }

    @Test
    void testDeleteFile_NotFound() {
        // Given
        int fileId = 999;
        int expectedResult = 0;
        when(fileMapper.deleteFile(fileId)).thenReturn(expectedResult);

        // When
        int actualResult = fileService.deleteFile(fileId);

        // Then
        assertEquals(expectedResult, actualResult);
        verify(fileMapper, times(1)).deleteFile(fileId);
    }



    // 辅助方法：创建另一个测试文件
    private File createAnotherFile() {
        File anotherFile = new File();
        anotherFile.setId(2);
        anotherFile.setName("another-file.mp4");
        anotherFile.setFileUrl("/files/another-file.mp4");
        anotherFile.setFileType("VIDEO");
        anotherFile.setOwnerType("CHAPTER");
        anotherFile.setOwnerId(200);
        anotherFile.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        anotherFile.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return anotherFile;
    }
}