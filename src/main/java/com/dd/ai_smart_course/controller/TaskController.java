package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.entity.Result;
import com.dd.ai_smart_course.entity.Task;
import com.dd.ai_smart_course.service.TaskService;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@Slf4j
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public Result<List<Task>> insertBatch(@RequestBody List<Task> tasks){
        log.info("insertBatch: {}", tasks);
        taskService.insertBatch(tasks);
        return Result.success(tasks);
    }

    @GetMapping("/{courseId}")
    @ApiOperation("通过课程id获取任务列表")
    public Result<List< Task>> listByCourseId(@PathVariable int courseId){
        log.info("listByCourseId: {}", courseId);
        List<Task> tasks = taskService.listByCourseId(courseId);
        return Result.success(tasks);
    }

    @DeleteMapping("/delete/{taskId}")
    public Result delete(@PathVariable int taskId){
        log.info("delete: {}", taskId);
        taskService.delete(taskId);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Task task){
        log.info("update: {}", task);
        taskService.update(task);
        return Result.success();
    }








}
