package com.dd.ai_smart_course.service.impl;
import com.dd.ai_smart_course.dto.CoursesDTO;
import com.dd.ai_smart_course.entity.*;
import com.dd.ai_smart_course.event.LearningActionEvent;
import com.dd.ai_smart_course.mapper.*;
import com.dd.ai_smart_course.service.base.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CourseImplTest {
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private ChapterMapper chapterMapper;
    @Mock
    private ConceptMapper conceptMapper;
    @Mock
    private QAMapper qaMapper;
    @Mock
    private CourseUserMapper courseUserMapper;
    @Mock
    private ScoreMapper scoreMapper;
    @Mock
    private LogMapper logMapper;
    @InjectMocks
    private CourseImpl courseService;
    private Course testCourse;
    private Chapter testChapter;
    private Concept testConcept;
    private Course_user testCourseUser;
    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setId(1);
        testCourse.setName("Test Course");
        testCourse.setTeacherId(1);
        testCourse.setDescription("Test Description");
        testCourse.setStatus("published");
        testCourse.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        testChapter = new Chapter();
        testChapter.setId(1);
        testChapter.setCourseId(1);
        testChapter.setTitle("Test Chapter");
        testConcept = new Concept();
        testConcept.setId(1);
        testConcept.setChapterId(1);
        testConcept.setName("Test Concept");
        testCourseUser = new Course_user(1L, 1L, "STUDENT");
    }
    @Test
    void getAllCourse_ShouldReturnAllCourses() {
        // Given
        List<Course> expectedCourses = Arrays.asList(testCourse);
        when(courseMapper.getAllCourses()).thenReturn(expectedCourses);
        // When
        List<Course> actualCourses = courseService.getAllCourse();
        // Then
        assertEquals(expectedCourses, actualCourses);
        verify(courseMapper).getAllCourses();
    }
    @Test
    void getCourseById_ShouldReturnCourse() {
        // Given
        when(courseMapper.getCourseById(1)).thenReturn(testCourse);
        // When
        Course actualCourse = courseService.getCourseById(1);
        // Then
        assertEquals(testCourse, actualCourse);
        verify(courseMapper).getCourseById(1);
    }
    @Test
    void addCourse_ShouldReturnInsertedRows() {
        // Given
        when(courseMapper.addCourse(testCourse)).thenReturn(1);
        // When
        int result = courseService.addCourse(testCourse);
        // Then
        assertEquals(1, result);
        verify(courseMapper).addCourse(testCourse);
    }
    @Test
    void updateCourse_ShouldReturnUpdatedRows() {
        // Given
        when(courseMapper.updateCourse(testCourse)).thenReturn(1);
        // When
        int result = courseService.updateCourse(testCourse);
        // Then
        assertEquals(1, result);
        verify(courseMapper).updateCourse(testCourse);
    }
    @Test
    void deleteCourse_WithValidId_ShouldDeleteSuccessfully() {
        // Given
        int courseId = 1;
        when(courseMapper.getCourseById(courseId)).thenReturn(testCourse);
        when(qaMapper.deleteByCourseId(courseId)).thenReturn(2);
        when(conceptMapper.deleteByCourseId(courseId)).thenReturn(3);
        when(chapterMapper.deleteByCourseId(courseId)).thenReturn(1);
        when(courseMapper.deleteCourse(courseId)).thenReturn(1);
        // When
        int result = courseService.deleteCourse(courseId);
        // Then
        assertEquals(1, result);
        verify(courseMapper).getCourseById(courseId);
        verify(qaMapper).deleteByCourseId(courseId);
        verify(conceptMapper).deleteByCourseId(courseId);
        verify(chapterMapper).deleteByCourseId(courseId);
        verify(courseMapper).deleteCourse(courseId);
    }
    @Test
    void deleteCourse_WithInvalidId_ShouldThrowException() {
        // Given
        int invalidId = -1;
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> courseService.deleteCourse(invalidId)
        );
        assertEquals("Course ID must be positive", exception.getMessage());
    }
    @Test
    void deleteCourse_WithNonExistentCourse_ShouldReturnZero() {
        // Given
        int courseId = 999;
        when(courseMapper.getCourseById(courseId)).thenReturn(null);
        // When
        int result = courseService.deleteCourse(courseId);
        // Then
        assertEquals(0, result);
        verify(courseMapper).getCourseById(courseId);
        verify(qaMapper, never()).deleteByCourseId(courseId);
    }
    @Test
    void deleteCourse_WithException_ShouldThrowRuntimeException() {
        // Given
        int courseId = 1;
        when(courseMapper.getCourseById(courseId)).thenReturn(testCourse);
        when(qaMapper.deleteByCourseId(courseId)).thenThrow(new RuntimeException("Database error"));
        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> courseService.deleteCourse(courseId)
        );
        assertTrue(exception.getMessage().contains("Failed to delete course and related data"));
    }

    @Test
    void getChaptersByCourse_ShouldReturnChapters() {
        // Given
        int courseId = 1;
        List<Chapter> expectedChapters = Arrays.asList(testChapter);
        when(courseMapper.findChaptersForGrouping(courseId)).thenReturn(expectedChapters);
        // When
        List<Chapter> actualChapters = courseService.getChaptersByCourse(courseId);
        // Then
        assertEquals(expectedChapters, actualChapters);
        verify(courseMapper).findChaptersForGrouping(courseId);
    }
    @Test
    void getConceptsByCourse_ShouldReturnConcepts() {
        // Given
        int  courseId = 1;
        List<Concept> expectedConcepts = Arrays.asList(testConcept);
        when(courseMapper.getConceptsByCourse(courseId)).thenReturn(expectedConcepts);
        // When
        List<Concept> actualConcepts = courseService.getConceptsByCourse(courseId);
        // Then
        assertEquals(expectedConcepts, actualConcepts);
        verify(courseMapper).getConceptsByCourse(courseId);
    }
    @Test
    void getConceptsGroupedByChapter_ShouldReturnGroupedConcepts() {
        // Given
        int courseId = 1;
        List<Chapter> chapters = Arrays.asList(testChapter);
        List<Concept> concepts = Arrays.asList(testConcept);
        when(courseMapper.findChaptersForGrouping(courseId)).thenReturn(chapters);
        when(courseMapper.getConceptsByCourse(courseId)).thenReturn(concepts);
        // When
        Map<Chapter, List<Concept>> result = courseService.getConceptsGroupedByChapter(courseId);
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey(testChapter));
        assertEquals(1, result.get(testChapter).size());
        assertEquals(testConcept, result.get(testChapter).get(0));
    }
    @Test
    void enrollUserInCourse_WithNewUser_ShouldEnrollSuccessfully() {
        // Given
        int userId = 1;
        int courseId = 1;
        when(courseUserMapper.findByCourseIdAndUserId(courseId, userId)).thenReturn(Optional.empty());
        // When
        courseService.enrollUserInCourse(userId, courseId);
        // Then
        verify(courseUserMapper).insertCourseUser(any(Course_user.class));
        verify(eventPublisher).publishEvent(any(LearningActionEvent.class));
    }
    @Test
    void enrollUserInCourse_WithExistingUser_ShouldThrowException() {
        // Given
        int userId = 1;
        int courseId = 1;
        when(courseUserMapper.findByCourseIdAndUserId(courseId, userId))
                .thenReturn(Optional.of(testCourseUser));
        // When & Then
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> courseService.enrollUserInCourse(userId, courseId)
        );
        assertEquals("User already enrolled in this course.", exception.getMessage());
    }

    @Test
    void unenrollUserFromCourse_WithNonStudent_ShouldThrowException() {
        // Given
        int userId = 1;
        int courseId = 1;
        Course_user teacher = new Course_user(1L, 1L, "TEACHER");
        when(courseUserMapper.findByCourseIdAndUserId(courseId, userId))
                .thenReturn(Optional.of(teacher));
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> courseService.unenrollUserFromCourse(userId, courseId)
        );
        assertEquals("User is not a student of this course or not enrolled.", exception.getMessage());
    }

    @Test
    void startViewingChapter_WithValidChapter_ShouldStartViewingSuccessfully() {
        // Given
        int chapterId = 1;
        int userId = 1;
        int courseId = 1; // Assuming courseId is needed to find Course_user
        testChapter.setCourseId(courseId); // Ensure testChapter has a courseId
        when(chapterMapper.findById(chapterId)).thenReturn(Optional.of(testChapter));
        when(courseUserMapper.findByCourseIdAndUserId(courseId, userId))
                .thenReturn(Optional.of(testCourseUser));
        // When
        courseService.startViewingChapter(chapterId, userId);
        // Then
        verify(eventPublisher).publishEvent(any(LearningActionEvent.class));
    }

    @Test
    void startViewingChapter_WithChapterNotFound_ShouldThrowException() {
        // Given
        int chapterId = 999;
        int userId = 1;
        when(chapterMapper.findById(chapterId)).thenReturn(Optional.empty());
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> courseService.startViewingChapter(chapterId, userId)
        );
        assertEquals("Chapter not found: " + chapterId, exception.getMessage());
    }


    @Test
    void completeChapter_WithValidChapter_ShouldCompleteSuccessfully() {
        // Given
        int chapterId = 1;
        int userId = 1;
        int courseId = 1; // Assuming courseId is needed to find Course_user
        testChapter.setCourseId(courseId); // Ensure testChapter has a courseId
        when(chapterMapper.findById(chapterId)).thenReturn(Optional.of(testChapter));
        when(courseUserMapper.findByCourseIdAndUserId(courseId, userId))
                .thenReturn(Optional.of(testCourseUser));
        // When
        courseService.completeChapter(chapterId, userId);
        // Then
        verify(eventPublisher).publishEvent(any(LearningActionEvent.class));
    }

    @Test
    void completeChapter_WithChapterNotFound_ShouldThrowException() {
        // Given
        int chapterId = 999;
        int userId = 1;
        when(chapterMapper.findById(chapterId)).thenReturn(Optional.empty());
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> courseService.completeChapter(chapterId, userId)
        );
        assertEquals("Chapter not found: " + chapterId, exception.getMessage());
    }



    @Test
    void enrollUserInCourse_ShouldPublishCorrectEvent() {
        // Given
        int userId = 1;
        int courseId = 1;
        when(courseUserMapper.findByCourseIdAndUserId(courseId, userId)).thenReturn(Optional.empty());
        // When
        courseService.enrollUserInCourse(userId, courseId);
        // Then
        // Verify the exact properties of the published event using argThat
        verify(eventPublisher).publishEvent(argThat(event -> {
            if (event instanceof LearningActionEvent) {
                LearningActionEvent lae = (LearningActionEvent) event;
                return lae.getUserId() == userId &&
                        lae.getTargetType().equals("course") &&
                        lae.getTargetId() == courseId &&
                        lae.getActionType().equals("click") &&
                        lae.getDetail().contains("ENROLL");
            }
            return false;
        }));
    }

}