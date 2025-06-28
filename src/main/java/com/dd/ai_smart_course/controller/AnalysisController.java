package com.dd.ai_smart_course.controller;

import com.dd.ai_smart_course.component.JwtTokenUtil;
import com.dd.ai_smart_course.dto.LearningLogDTO;
import com.dd.ai_smart_course.dto.LearningStatsDTO;
import com.dd.ai_smart_course.R.PaginationResult;
import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // 暂时硬编码用户ID，实际应该从JWT或Session中获取
    private int getCurrentUserId() {
        return 1; // TODO: 从认证信息中获取
    }

    /**
     * 获取当前用户的学习日志
     */
    @GetMapping("/my-logs")
    public Result<?> getMyLearningLogs(
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) String actionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int userId = getCurrentUserId();
        // 调用Service
        var result = analysisService.getLearningLogs(userId, targetType, actionType, startTime, endTime, page, size);
        return Result.success("获取成功", result);
    }

    /**
     * 获取所有学习日志（管理员功能）
     */
    @GetMapping("/all-logs")
    public Result<?> getAllLearningLogs(
            @RequestParam(required = false) int userId,
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) String actionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = analysisService.getLearningLogs(userId, targetType, actionType, startTime, endTime, page, size);
        return Result.success("获取成功", result);
    }

    /**
     * 统计所有学习日志数量
     */
    @GetMapping("/all-logs/count")
    public Result<Long> countAllLearningLogs(
            @RequestParam(required = false) int userId,
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) String actionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime
    ) {
        PaginationResult<LearningLogDTO> result = analysisService.getLearningLogs(userId, targetType, actionType, startTime, endTime, 0, 1);
        long count = result != null ? result.getTotalElements() : 0;
        return Result.success("统计成功", count);
    }

    /**
     * 获取当前用户的学习统计数据
     */
    @GetMapping("/my-stats")
    public Result<LearningStatsDTO> getMyLearningStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime
    ) {
        int userId = getCurrentUserId();
        LearningStatsDTO stats = analysisService.getLearningStats(userId, startTime, endTime);
        return Result.success("获取成功", stats);
    }

    /**
     * 获取指定用户的学习统计数据（管理员功能）
     */
    @GetMapping("/user-stats/{userId}")
    public Result<LearningStatsDTO> getUserLearningStats(
            @PathVariable int userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime
    ) {
        LearningStatsDTO stats = analysisService.getLearningStats(userId, startTime, endTime);
        return Result.success("获取成功", stats);
    }
}
