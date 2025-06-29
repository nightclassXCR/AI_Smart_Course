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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
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
    public Result<CoursesDTO> getCourseById(@PathVariable int id) {
        Course course = courseService.getCourseById(id);
        CoursesDTO coursesDTO = new CoursesDTO();
        BeanUtils.copyProperties(course, coursesDTO);
        coursesDTO.setTeacherRealName(courseService.getUserNameById(course.getTeacherId()));
        return Result.success(coursesDTO);
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
    public Result<List<Course>> getCoursesByTeacherId(@PathVariable("teacherId") int teacherId) {
        List<Course> courses = courseService.getCoursesByTeacherId(teacherId);
        return Result.success("获取成功", courses);
    }

    /**
     * 获取课程下所有章节
     * @param courseId 课程ID
     * @return 章节列表
     */
    @GetMapping("/chapters/{courseId}")
    public Result<List<Chapter>> getChaptersByCourse(@PathVariable("courseId") int courseId) {
        List<Chapter> chapters = courseService.getChaptersByCourse(courseId);
        return Result.success("获取成功", chapters);
    }

    /**
     * 获取课程下所有概念
     * @param courseId 课程ID
     * @return 概念列表
     */
    @GetMapping("/concepts/{courseId}")
    public Result<List<Concept>> getConceptsByCourse(@PathVariable("courseId") int courseId) {
        List<Concept> concepts = courseService.getConceptsByCourse(courseId);
        return Result.success("获取成功", concepts);
    }

    /**
     * 按章节分组的知识点
     * @param courseId 课程ID
     * @return 按章节分组的知识点Map
     */
    @GetMapping("/groupedConcepts/{courseId}")
    public Result<Map<Integer, List<Concept>>> getConceptsGroupedByChapter(@PathVariable("courseId") int courseId) {
        Map<Chapter, List<Concept>> map = courseService.getConceptsGroupedByChapter(courseId);
        // 新建一个以章节id为key的map
        Map<Integer, List<Concept>> idKeyMap = new HashMap<>();
        for (Map.Entry<Chapter, List<Concept>> entry : map.entrySet()) {
            idKeyMap.put(entry.getKey().getId(), entry.getValue());
        }
        return Result.success("获取成功", idKeyMap);
    }

    /**
     * 用户选课
     * @param courseId 课程ID
     * @return 选课结果
     */
    @PostMapping("/enroll/{courseId}")
    public Result<String> enrollUserInCourse(HttpServletRequest request,@PathVariable("courseId") int courseId) {
        // 从请求头中获取 JWT 令牌
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error("缺少或无效的令牌");
        }

        String token = authHeader.substring(7); // 去掉 "Bearer " 前缀
        Integer userId = jwtTokenUtil.getUserIDFromToken(token);

        if (userId == null) {
            return Result.error("无效的令牌");
        }
        courseService.enrollUserInCourse(userId, courseId);
        return Result.success("选课成功");
    }
    /**
     * 用户退课
     * @param courseId 课程ID
     * @return 退课结果
     */
    @PostMapping("/unenroll/{courseId}")
    public Result<String> unenrollUserFromCourse(HttpServletRequest request,@PathVariable("courseId") int courseId) {
        // 从请求头中获取 JWT 令牌
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error("缺少或无效的令牌");
        }

        String token = authHeader.substring(7); // 去掉 "Bearer " 前缀
        Integer userId = jwtTokenUtil.getUserIDFromToken(token);

        courseService.unenrollUserFromCourse(userId, courseId);
        return Result.success("退课成功");
    }

    /**
     * 获取我的课程
     * @return 用户已选课程列表
     */
    @GetMapping("/my-courses")
    public Result<List<CoursesDTO>> getMyCourses(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        String token = authHeader != null ? authHeader.replace("Bearer ", "").trim() : null;
        Integer userId = jwtTokenUtil.getUserIDFromToken(token);
        log.info("userId: {}", userId);
        log.info("我的课程列表: {}", courseService.getMyCourses(userId));
        List<CoursesDTO> myCourses = courseService.getMyCourses(userId);
        return Result.success(myCourses);
    }

    @GetMapping("/pagination")
    public PaginationResult<Course> getCourses(@RequestParam(defaultValue = "1") int pageNum,
                                             @RequestParam(defaultValue = "10") int pageSize) {
        return courseService.getCourses(pageNum, pageSize);
    }
    /**
     * 模糊查询在用户已有的课程进行查询
     * @param keyword 关键词
     */
    @GetMapping("/search")
    public List<CoursesDTO> searchCourses(
            @RequestParam("keyword") String keyword,
            @RequestParam("userId") int userId) {

        System.out.println("userId: " + userId);
        List<CoursesDTO> myCourses = courseService.searchCourses(keyword, userId); // 返回 DTO 列表
        System.out.println("我的课程列表: " + myCourses); // 打印 DTO 列表
        return myCourses;
    }

    /**
     * 查询不是我所选的所有课程
     * @return
     */
    @GetMapping("/NotMyCourses")
    public Result<List<CoursesDTO>> getCoursesNotMyCourses(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader != null ? authHeader.replace("Bearer ", "").trim() : null;
        Integer userId = jwtTokenUtil.getUserIDFromToken(token);
        List<CoursesDTO> courses = courseService.getCoursesNotMyCourses(userId);
        log.info("我的课程列表: {}", courses);
        return Result.success("获取成功", courses);
    }


    /**
     * 根据教师ID获取课程名
     * @return
     */
    @GetMapping("/getCourseNameByUserId")
    public Result<List<Course>> getCourseNameByUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader != null ? authHeader.replace("Bearer ", "").trim() : null;
        Integer userId = jwtTokenUtil.getUserIDFromToken(token);
        log.info("userId: {}", userId);
        log.info("课程名: {}", courseService.getCoursesByTeacherId(userId));
        return Result.success(courseService.getCoursesByTeacherId(userId));
    }

    /**
     * 获取用户已选课程的课程名
     * @return
     */
    @GetMapping("/getMyCompletedCourse")
    public Result<Integer> getMyCompletedCourse(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        String token = authHeader != null ? authHeader.replace("Bearer ", "").trim() : null;
        Integer userId = jwtTokenUtil.getUserIDFromToken(token);
        return Result.success("获取成功", courseService.getCompletedCourseCount(userId));
    }




}
