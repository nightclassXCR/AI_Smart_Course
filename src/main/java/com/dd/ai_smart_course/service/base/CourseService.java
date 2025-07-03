package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.R.PaginationResult;
import com.dd.ai_smart_course.dto.CoursesDTO;
import com.dd.ai_smart_course.entity.Chapter;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Course;
import com.dd.ai_smart_course.entity.User;

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
    List<CoursesDTO> getCoursesByTeacherId(int teacherId);
    //在数据库获取的课程列表中补充上学生数目
    List<CoursesDTO> getCoursesWithStudentCount(List<CoursesDTO> courses);

    // 获取课程下所有章节
    List<Chapter> getChaptersByCourse(int courseId);
    // 获取课程下所有知识点
    List<Concept> getConceptsByCourse(int courseId);
    // 按章节分组的知识点
    Map<Chapter, List<Concept>> getConceptsGroupedByChapter(int courseId);

    // 用户选课
    void enrollUserInCourse(int userId, int courseId);

    // 用户退课
    void unenrollUserFromCourse(int userId, int courseId);

    // 用户是否完成课程
    void completeCourse(int courseId, int userId);

    // 用户开始查看章节
    void startViewingChapter(int chapterId, int userId);

    // 学生获取课程
    List<CoursesDTO> getMyCourses(int userId);

    // 分页获取课程
    PaginationResult<Course> getCourses(int pageNum, int pageSize);

    // 搜索课程
    List<CoursesDTO> searchCourses(String keyword, int userId);
    // 获取未选课程
    List<CoursesDTO> getCoursesNotMyCourses(int userId);
    // 根据ID获取用户名
    String getUserNameById(int userId);

    // 结课
    void comleteCourse(int courseId);

    // 获取已结课课程数
    int getCompletedCourseCount(int userId);

    // 获取指定教师课程数
    int getCouresCountByTeacherId(int teacherId);

    // 获取某门课程下的所有学生ID
    List<Integer> getStudentsIDByCourseId(int courseId);

    // 获取某门课程下的所有学生User实体
    List<User> getStudentsByCourseId(int courseId);
}
