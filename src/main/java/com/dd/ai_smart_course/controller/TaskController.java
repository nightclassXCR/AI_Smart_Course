package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.dto.TaskDTO;
import com.dd.ai_smart_course.entity.Task;
import com.dd.ai_smart_course.mapper.CourseMapper;
import com.dd.ai_smart_course.service.base.TaskService;
//import io.swagger.annotations.ApiOperation;
import com.dd.ai_smart_course.utils.BaseContext;
import com.dd.ai_smart_course.vo.TaskVO;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@RestController
@Slf4j
@RequestMapping("/homework")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private CourseMapper courseMapper;

    @PostMapping("/create")
    public Result<TaskDTO> createTask(@RequestBody TaskDTO taskDTO){
        log.info("insertBatch: {}", taskDTO);
        taskService.insert(taskDTO);
        return Result.success(taskDTO);
    }



    @GetMapping("/{courseId}")
    //@ApiOperation("通过课程id获取任务列表")
    public Result<List<Task>> listByCourseId(@PathVariable int courseId){
        log.info("listByCourseId: {}", courseId);
        List<Task> tasks = taskService.listByCourseId(courseId);
        return Result.success(tasks);
    }

    @GetMapping
    //@ApiOperation("通过课程id获取任务列表")
    public Result<List<Task>> listByCourseId(){
        int courseId = 14;
        List<Task> tasks = taskService.listByCourseId(courseId);
        log.info("listByCourseId:{}", tasks);
        return Result.success(tasks);
    }
    @GetMapping("/list")
    //@ApiOperation("通过user id获取任务列表")
    public Result<List<TaskVO>> listByUserId(){
        int userId= BaseContext.getCurrentId();
        log.info("listByUserId:{}", userId);
        List<Task> tasks = taskService.listByUserId(userId);
        List<TaskVO> tasksVO = new ArrayList<>();
        for (Task task : tasks) {
            TaskVO taskVO = new TaskVO();
            BeanUtils.copyProperties(task, taskVO);
            taskVO.setCourseName(courseMapper.getCourseById(task.getCourseId()).getName());
            log.info("taskVO:{}", taskVO);
            tasksVO.add(taskVO);
        }
        log.info("listByCourseId:{}", tasks);
        return Result.success(tasksVO);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable int id){
        log.info("delete: {}", id);
        taskService.delete(id);
        return Result.success();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Task task){
        log.info("update: {}",task);
        taskService.update(task);
        return Result.success();
    }

    @GetMapping("/count")
    public Result<Integer> getTaskCountByTeacherId(){
        int teacherId = BaseContext.getCurrentId();
        return Result.success(taskService.getTaskCountByTeacherId(teacherId));
    }












}
