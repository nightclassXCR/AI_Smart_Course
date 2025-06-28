//package com.dd.ai_smart_course.service.impl;
//
//import com.dd.ai_smart_course.entity.Resource;
//import com.dd.ai_smart_course.entity.Resource.FileType;
//import com.dd.ai_smart_course.entity.Resource.OwnerType;
//import com.dd.ai_smart_course.mapper.ResourceMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ResourceServiceImplTest {
//
//    @Mock
//    private ResourceMapper resourceMapper;
//
//    @InjectMocks
//    private ResourceServiceImpl resourceService;
//
//    private Resource testResource;
//
//    @BeforeEach
//    void setUp() {
//        testResource = new Resource();
//        testResource.setId(1L);
//        testResource.setName("Test Document");
//        testResource.setFileUrl("/path/to/test_doc.pdf");
//        testResource.setFileType(FileType.PDF);
//        testResource.setOwnerId(100L);
//        testResource.setOwnerType(OwnerType.COURSE);
//        testResource.setCreatedAt(LocalDateTime.now().minusHours(2));
//        testResource.setUpdatedAt(LocalDateTime.now().minusHours(1));
//    }
//
//    @Test
//    void testSave_Success() {
//        // Given
//        Resource newResource = new Resource();
//        newResource.setName("New Video");
//        newResource.setFileUrl("/path/to/new_video.mp4");
//        newResource.setFileType(FileType.VIDEO);
//        newResource.setOwnerId(200L);
//        newResource.setOwnerType(OwnerType.CHAPTER);
//
//        // When
//        resourceService.save(newResource);
//
//        // Then
//        ArgumentCaptor<Resource> resourceCaptor = ArgumentCaptor.forClass(Resource.class);
//        verify(resourceMapper, times(1)).insert(resourceCaptor.capture());
//
//        Resource capturedResource = resourceCaptor.getValue();
//        assertNotNull(capturedResource.getCreatedAt()); // Should be set by the service
//        assertEquals(newResource.getName(), capturedResource.getName());
//        assertEquals(newResource.getFileUrl(), capturedResource.getFileUrl());
//        assertEquals(newResource.getFileType(), capturedResource.getFileType());
//        assertEquals(newResource.getOwnerId(), capturedResource.getOwnerId());
//        assertEquals(newResource.getOwnerType(), capturedResource.getOwnerType());
//        // UpdatedAt is not set in save method, so it should be null or default
//        assertNull(capturedResource.getUpdatedAt());
//    }
//
//    @Test
//    void testFindById_Success() {
//        // Given
//        Long resourceId = 1L;
//        when(resourceMapper.selectById(resourceId)).thenReturn(testResource);
//
//        // When
//        Resource foundResource = resourceService.findById(resourceId);
//
//        // Then
//        assertNotNull(foundResource);
//        assertEquals(testResource.getId(), foundResource.getId());
//        assertEquals(testResource.getName(), foundResource.getName());
//        verify(resourceMapper, times(1)).selectById(resourceId);
//    }
//
//    @Test
//    void testFindById_NotFound() {
//        // Given
//        Long resourceId = 999L;
//        when(resourceMapper.selectById(resourceId)).thenReturn(null);
//
//        // When
//        Resource foundResource = resourceService.findById(resourceId);
//
//        // Then
//        assertNull(foundResource);
//        verify(resourceMapper, times(1)).selectById(resourceId);
//    }
//
//    @Test
//    void testDelete_Success() {
//        // Given
//        Long resourceId = 1L;
//
//        // When
//        resourceService.delete(resourceId);
//
//        // Then
//        verify(resourceMapper, times(1)).deleteById(resourceId);
//    }
//
//    @Test
//    void testFilter_WithResults() {
//        // Given
//        Resource filterCriteria = new Resource();
//        filterCriteria.setFileType(FileType.PDF);
//        filterCriteria.setOwnerType(OwnerType.COURSE);
//
//        Resource anotherPdfResource = new Resource();
//        anotherPdfResource.setId(2L);
//        anotherPdfResource.setName("Another PDF");
//        anotherPdfResource.setFileUrl("/path/to/another_pdf.pdf");
//        anotherPdfResource.setFileType(FileType.PDF);
//        anotherPdfResource.setOwnerType(OwnerType.COURSE);
//        anotherPdfResource.setCreatedAt(LocalDateTime.now().minusDays(1));
//
//        List<Resource> expectedResources = Arrays.asList(testResource, anotherPdfResource);
//        when(resourceMapper.selectByFilter(any(Resource.class))).thenReturn(expectedResources);
//
//        // When
//        List<Resource> actualResources = resourceService.filter(filterCriteria);
//
//        // Then
//        assertNotNull(actualResources);
//        assertEquals(2, actualResources.size());
//        assertTrue(actualResources.contains(testResource));
//        assertTrue(actualResources.contains(anotherPdfResource));
//        verify(resourceMapper, times(1)).selectByFilter(any(Resource.class));
//    }
//
//    @Test
//    void testFilter_NoResults() {
//        // Given
//        Resource filterCriteria = new Resource();
//        filterCriteria.setFileType(FileType.IMAGE); // A type that won't match test data
//
//        when(resourceMapper.selectByFilter(any(Resource.class))).thenReturn(Collections.emptyList());
//
//        // When
//        List<Resource> actualResources = resourceService.filter(filterCriteria);
//
//        // Then
//        assertNotNull(actualResources);
//        assertTrue(actualResources.isEmpty());
//        verify(resourceMapper, times(1)).selectByFilter(any(Resource.class));
//    }
//
//    @Test
//    void testFilter_NoCriteria() {
//        // Given
//        List<Resource> allResources = Arrays.asList(testResource, createAnotherResource());
//        when(resourceMapper.selectByFilter(any(Resource.class))).thenReturn(allResources);
//
//        // When
//        List<Resource> actualResources = resourceService.filter(new Resource()); // Empty criteria
//
//        // Then
//        assertNotNull(actualResources);
//        assertFalse(actualResources.isEmpty());
//        assertEquals(2, actualResources.size());
//        verify(resourceMapper, times(1)).selectByFilter(any(Resource.class));
//    }
//
//    // Helper method to create another test Resource object
//    private Resource createAnotherResource() {
//        Resource anotherResource = new Resource();
//        anotherResource.setId(3L);
//        anotherResource.setName("Chapter Quiz");
//        anotherResource.setFileUrl("/path/to/quiz_doc.doc");
//        anotherResource.setFileType(FileType.DOC);
//        anotherResource.setOwnerId(201L);
//        anotherResource.setOwnerType(OwnerType.CHAPTER);
//        anotherResource.setCreatedAt(LocalDateTime.now().minusDays(3));
//        anotherResource.setUpdatedAt(LocalDateTime.now().minusDays(2));
//        return anotherResource;
//    }
//}