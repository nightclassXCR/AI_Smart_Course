package com.dd.ai_smart_course.service;

import com.dd.ai_smart_course.entity.Course;

import java.util.List;

public interface CourseService {
    // 获取所有课程
    List<Course> getAllCourse();
    // 获取课程详情
    Course getCourseById(int id);
    // 添加课程
    int addCourse(Course course);
    // 更新课程信息
    int updateCourse(Course course);
    // 删除课程
    int deleteCourse(int id);

}
