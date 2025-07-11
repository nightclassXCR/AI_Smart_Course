package com.dd.ai_smart_course.ControllerTest;

import com.dd.ai_smart_course.controller.CourseController;
import com.dd.ai_smart_course.dto.CoursesDTO;
import com.dd.ai_smart_course.entity.Chapter;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Course;
import com.dd.ai_smart_course.service.base.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CourseControllerTest {

    private MockMvc mockMvc;
    private CourseService courseService;
    private ObjectMapper objectMapper;
    private CourseController courseController;

    @BeforeEach
    public void setUp() throws Exception {
        courseService = mock(CourseService.class);
        objectMapper = new ObjectMapper();
        courseController = new CourseController();

        // 手动注入 CourseService
        var field = CourseController.class.getDeclaredField("courseService");
        field.setAccessible(true);
        field.set(courseController, courseService);

        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    public void testGetAllCourse() throws Exception {
        when(courseService.getAllCourse()).thenReturn(List.of(new Course()));

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetCourseById() throws Exception {
        when(courseService.getCourseById(1)).thenReturn(new Course());

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }


    @Test
    public void testDeleteCourseSuccess() throws Exception {
        when(courseService.deleteCourse(1)).thenReturn(1);

        mockMvc.perform(delete("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("操作成功"));
    }

    @Test
    public void testDeleteCourseFail() throws Exception {
        when(courseService.deleteCourse(1)).thenReturn(0);

        mockMvc.perform(delete("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("删除失败"));
    }

    @Test
    public void testGetChaptersByCourse() throws Exception {
        when(courseService.getChaptersByCourse(1)).thenReturn(List.of(new Chapter()));

        mockMvc.perform(get("/courses/chapters/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("获取成功"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetConceptsByCourse() throws Exception {
        when(courseService.getConceptsByCourse(1)).thenReturn(List.of(new Concept()));

        mockMvc.perform(get("/courses/concepts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("获取成功"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetConceptsGroupedByChapter() throws Exception {
        Chapter chapter = new Chapter();
        Map<Chapter, List<Concept>> groupedMap = Map.of(chapter, List.of(new Concept()));

        when(courseService.getConceptsGroupedByChapter(1)).thenReturn(groupedMap);

        mockMvc.perform(get("/courses/groupedConcepts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("获取成功"))
                .andExpect(jsonPath("$.data").exists());
    }
}
