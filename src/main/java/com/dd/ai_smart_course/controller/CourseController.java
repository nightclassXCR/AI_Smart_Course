package com.dd.ai_smart_course.controller;


import com.dd.ai_smart_course.R.PaginationResult;
import com.dd.ai_smart_course.dto.CoursesDTO;
import com.dd.ai_smart_course.entity.Chapter;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Course;
import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.service.base.CourseService;
import com.dd.ai_smart_course.component.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
@Slf4j
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 获取所有课程
     * @return 所有课程
     */
    @GetMapping
    public Result<List<Course>>getAllCourse() {
        return Result.success(courseService.getAllCourse());
    }

    /**
     * 获取课程详情
     * @param id 课程id
     * @return 课程详情
     */
    @GetMapping("/{id}")
    public Result<Course> getCourseById(@PathVariable int id) {
        return Result.success(courseService.getCourseById(id));
    }

    /**
     * 添加课程
     * @param course 课程信息
     * @return 添加结果
     */
    @PostMapping
    public Result<String> addCourse(@RequestBody Course course) {
        log.info("Adding course: {}", course);
        if(courseService.addCourse(course) > 0){
            return Result.success("添加成功");
        }else {
            return Result.error("添加失败");
        }
    }

    /**
     * 更新课程信息
     * @param course 课程信息
     * @return 更新结果
     */
    @PutMapping
    public Result<String> updateCourse(@RequestBody Course course) {
        if(courseService.updateCourse(course) > 0){
            return Result.success("更新成功");
        }else {
            return Result.error("更新失败");
        }
    }

    /**
     * 删除课程
     * @param id 课程id
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteCourse(@PathVariable int id) {
        if(courseService.deleteCourse(id) > 0){
            return Result.success("删除成功");
        }else {
            return Result.error("删除失败");
        }
    }

    /**
     * 获取指定教师授课的课程
     * @param teacherId 教师ID
     * @return 教师授课的课程列表
     */
    @GetMapping("/byTeacher/{teacherId}")
    public Result<List<Course>> getCoursesByTeacherId(@PathVariable("teacherId") Long teacherId) {
        List<Course> courses = courseService.getCoursesByTeacherId(teacherId);
        return Result.success("获取成功", courses);
    }

    /**
     * 获取课程下所有章节
     * @param courseId 课程ID
     * @return 章节列表
     */
    @GetMapping("/chapters/{courseId}")
    public Result<List<Chapter>> getChaptersByCourse(@PathVariable("courseId") Long courseId) {
        List<Chapter> chapters = courseService.getChaptersByCourse(courseId);
        return Result.success("获取成功", chapters);
    }

    /**
     * 获取课程下所有概念
     * @param courseId 课程ID
     * @return 概念列表
     */
    @GetMapping("/concepts/{courseId}")
    public Result<List<Concept>> getConceptsByCourse(@PathVariable("courseId") Long courseId) {
        List<Concept> concepts = courseService.getConceptsByCourse(courseId);
        return Result.success("获取成功", concepts);
    }

    /**
     * 按章节分组的知识点
     * @param courseId 课程ID
     * @return 按章节分组的知识点Map
     */
    @GetMapping("/groupedConcepts/{courseId}")
    public Result<Map<Chapter, List<Concept>>> getConceptsGroupedByChapter(@PathVariable("courseId") Long courseId) {
        Map<Chapter, List<Concept>> groupedConcepts = courseService.getConceptsGroupedByChapter(courseId);
        return Result.success("获取成功", groupedConcepts);
    }

    /**
     * 用户选课
     * @param courseId 课程ID
     * @return 选课结果
     */
    @PostMapping("/enroll/{courseId}")
    public Result<String> enrollUserInCourse(@PathVariable("courseId") Long courseId, @RequestBody Map<String, Long> requestBody) {
        Long userId = requestBody.get("userId");
        courseService.enrollUserInCourse(userId, courseId);
        return Result.success("选课成功");
    }
    /**
     * 用户退课
     * @param courseId 课程ID
     * @return 退课结果
     */
    @PostMapping("/unenroll/{courseId}")
    public Result<String> unenrollUserFromCourse(@PathVariable("courseId") Long courseId, @RequestBody Map<String, Long> requestBody) {
        Long userId = requestBody.get("userId");
        courseService.unenrollUserFromCourse(userId, courseId);
        return Result.success("退课成功");
    }

    /**
     * 获取我的课程
     * @return 用户已选课程列表
     */
    @GetMapping("/my-courses")
    public Result<List<CoursesDTO>> getMyCourses(HttpServletRequest request) {
        log.info("getMyCourses");
        String authHeader = request.getHeader("Authorization");
        String token = authHeader != null ? authHeader.replace("Bearer ", "").trim() : null;
        Integer userId = jwtTokenUtil.getUserIDFromToken(token);
        log.info("userId: {}", userId);
        log.info("我的课程列表: {}", courseService.getMyCourses(userId.longValue()));
        List<CoursesDTO> myCourses = courseService.getMyCourses(Long.valueOf(userId));
        return Result.success(myCourses);
    }

    @GetMapping("/pagination")
    public PaginationResult<Course> getCourses(@RequestParam(defaultValue = "1") int pageNum,
                                             @RequestParam(defaultValue = "10") int pageSize) {
        return courseService.getCourses(pageNum, pageSize);
    }
    /**
     * 模糊查询在用户已有的课程进行查询
     * 能查到但是会崩溃
     * @param keyword 关键词
     */
    @GetMapping("/search")
    public List<CoursesDTO> searchCourses(
            @RequestParam("keyword") String keyword,
            @RequestParam("userId") Long userId) {

        System.out.println("userId: " + userId);
        List<CoursesDTO> myCourses = courseService.searchCourses(keyword, userId); // 返回 DTO 列表
        System.out.println("我的课程列表: " + myCourses); // 打印 DTO 列表
        return myCourses;
    }

}
