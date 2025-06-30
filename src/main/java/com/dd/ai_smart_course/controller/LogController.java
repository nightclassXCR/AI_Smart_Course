package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.component.JwtTokenUtil;
import com.dd.ai_smart_course.dto.LearningLogDTO;
import com.dd.ai_smart_course.dto.request.SearchRequest;
import com.dd.ai_smart_course.entity.LearningLog;
import com.dd.ai_smart_course.service.impl.LogImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogImpl logService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    //查询所有学习日志
    @GetMapping("/getAll")
    public Result<List<LearningLog>> getAllLogs() {
        List<LearningLog> data = logService.getAllLogs();
        return Result.success(data);
    }

    //根据 ID 查询学习日志
    @GetMapping("/get/{id}")
    public Result<LearningLog> getLogById(@PathVariable int id) {
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
    public Result<Integer> deleteLog(@PathVariable int id) {
        Integer rowsAffected = logService.deleteLog(id);
        return Result.success(rowsAffected);
    }

    // 获取最新的学习记录
    @GetMapping("/latest/{courseId}")
    public Result<LearningLogDTO> getLatestLearningLogInCourse(HttpServletRequest request,@PathVariable int courseId) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader != null ? authHeader.replace("Bearer ", "").trim() : null;
        Integer userId = jwtTokenUtil.getUserIDFromToken(token);
        LearningLogDTO latestLog = logService.findLatestLearningLogInCourseDTO(userId, courseId);
        return Result.success(latestLog);
    }


}