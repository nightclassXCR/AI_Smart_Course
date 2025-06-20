package com.dd.ai_smart_course.controller;


import com.dd.ai_smart_course.entity.Chapter;
import com.dd.ai_smart_course.entity.Concept;
import com.dd.ai_smart_course.entity.Course;
import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
@Slf4j
public class CourseController {

    @Autowired
    private CourseService courseService;

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



}
