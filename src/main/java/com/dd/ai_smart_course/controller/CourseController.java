package com.dd.ai_smart_course.controller;


import com.dd.ai_smart_course.entity.Course;
import com.dd.ai_smart_course.entity.Result;
import com.dd.ai_smart_course.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@Slf4j
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public Result<List<Course>>getAllCourse() {
        return Result.success(courseService.getAllCourse());
    }

    @GetMapping("/{id}")
    public Result<Course> getCourseById(@PathVariable int id) {
        return Result.success(courseService.getCourseById(id));
    }

    @PostMapping
    public Result<String> addCourse(@RequestBody Course course) {
        if(courseService.addCourse(course) > 0){
            return Result.success("添加成功");
        }else {
            return Result.error("添加失败");
        }
    }

    @PutMapping
    public Result<String> updateCourse(@RequestBody Course course) {
        if(courseService.updateCourse(course) > 0){
            return Result.success("更新成功");
        }else {
            return Result.error("更新失败");
        }
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteCourse(@PathVariable int id) {
        if(courseService.deleteCourse(id) > 0){
            return Result.success("删除成功");
        }else {
            return Result.error("删除失败");
        }
    }
}
