package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.R.PaginationResult;
import com.dd.ai_smart_course.entity.Chapter;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Course;

import java.util.List;
import java.util.Map;

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
    // 获取指定教师授课的课程
    List<Course> getCoursesByTeacherId(Long teacherId);
    // 获取课程下所有章节
    List<Chapter> getChaptersByCourse(Long courseId);
    // 获取课程下所有知识点
    List<Concept> getConceptsByCourse(Long courseId);
    // 按章节分组的知识点
    Map<Chapter, List<Concept>> getConceptsGroupedByChapter(Long courseId);

    // 用户选课
    void enrollUserInCourse(Long userId, Long courseId);

    // 用户退课
    void unenrollUserFromCourse(Long userId, Long courseId);

    // 用户是否完成课程
    void completeCourse(Long courseId, Long userId);

    // 用户开始查看章节
    void startViewingChapter(Long chapterId, Long userId);

    // 学生获取课程
    List<Course> getMyCourses(Long userId);

    // 分页获取课程
    PaginationResult<Course> getCourses(int pageNum, int pageSize);

    // 搜索课程
    List<Course> searchCourses(String keyword, Long userId);
}
