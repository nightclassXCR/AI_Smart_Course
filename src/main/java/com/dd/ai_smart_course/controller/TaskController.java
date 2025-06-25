package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.entity.Resource;
import com.dd.ai_smart_course.entity.Task;
import com.dd.ai_smart_course.service.base.TaskService;
//import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@Slf4j
@RequestMapping("/homework")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public Result<List<Task>> createTask(@RequestBody List<Task> tasks){
        log.info("insertBatch: {}", tasks);
        taskService.insertBatch(tasks);
        return Result.success(tasks);
    }

    @GetMapping("/{courseId}")
    //@ApiOperation("通过课程id获取任务列表")
    public Result<List<Task>> listByCourseId(@PathVariable int courseId){
        log.info("listByCourseId: {}", courseId);
        List<Task> tasks = taskService.listByCourseId(courseId);
        return Result.success(tasks);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id){
        log.info("delete: {}", id);
        taskService.delete(id);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody Task task, @PathVariable int id){
        log.info("update: {}{}",id,task);
        task.setId(id);
        taskService.update(task);
        return Result.success();
    }












}
