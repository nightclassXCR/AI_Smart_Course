package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.dto.request.SearchRequest;
import com.dd.ai_smart_course.entity.LearningLog;
import com.dd.ai_smart_course.service.impl.LogImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogImpl logService;

    //查询所有学习日志
    @GetMapping("/getAll")
    public Result<List<LearningLog>> getAllLogs() {
        List<LearningLog> data = logService.getAllLogs();
        return Result.success(data);
    }

    //根据 ID 查询学习日志
    @GetMapping("/get/{id}")
    public Result<LearningLog> getLogById(@PathVariable long id) {
        LearningLog data = logService.getLogById(id);
        return Result.success(data);
    }

    //添加学习日志
    @PostMapping("/add")
    public Result<Integer> addLog(@RequestBody LearningLog learningLog) {
        try {
            Integer rowsAffected = logService.addLog(learningLog);
            return Result.success(rowsAffected);
        } catch (Exception e) {
            return Result.error(500, "添加学习日志失败：" + e.getMessage());
        }
    }

    //更新学习日志
    @PostMapping("/update")
    public Result<Integer> updateLog(@RequestBody LearningLog learningLog) {
        try {
            Integer rowsAffected = logService.updateLog(learningLog);
            return Result.success(rowsAffected);
        } catch (Exception e) {
            return Result.error(500, "更新学习日志失败：" + e.getMessage());
        }
    }

    //删除学习日志
    @GetMapping("/delete/{id}")
    public Result<Integer> deleteLog(@PathVariable long id) {
        Integer rowsAffected = logService.deleteLog(id);
        return Result.success(rowsAffected);
    }


}