package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.Chapter;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Course;
import com.dd.ai_smart_course.mapper.CourseMapper;
import com.dd.ai_smart_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CourseImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> getAllCourse() {
        return courseMapper.getAllCourses();
    }

    @Override
    public Course getCourseById(int id) {
        return courseMapper.getCourseById(id);
    }

    @Override
    public int addCourse(Course course) {
        return courseMapper.addCourse(course);
    }

    @Override
    public int updateCourse(Course course) {
        return courseMapper.updateCourse(course);
    }

    @Override
    public int deleteCourse(int id) {
        return courseMapper.deleteCourse(id);
    }

    // TODO: Implement
    @Override
    public List<Course> getCoursesByTeacherId(Long teacherId) {
        return List.of();
    }

    // TODO: Implement
    @Override
    public List<Chapter> getChaptersByCourse(Long courseId) {
        return List.of();
    }

    // TODO: Implement
    @Override
    public List<Concept> getConceptsByCourse(Long courseId) {
        return List.of();
    }

    // TODO: Implement
    @Override
    public Map<Chapter, List<Concept>> getConceptsGroupedByChapter(Long courseId) {
        return Map.of();
    }
}
