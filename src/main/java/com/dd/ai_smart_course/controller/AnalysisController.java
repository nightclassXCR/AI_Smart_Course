package com.dd.ai_smart_course.controller;


import com.dd.ai_smart_course.R.Result;
import com.dd.ai_smart_course.dto.LearningStatsDto;
import com.dd.ai_smart_course.entity.LearningLog;
import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    // 假设您有一个获取当前用户ID的方法，例如 SecurityUtils.getCurrentUserId()
    // 或者直接从 @AuthenticationPrincipal 中获取
    private Long getCurrentUserId(@AuthenticationPrincipal User currentUser) {
        // 在实际应用中，您需要根据您的 UserDetails 实现来获取用户ID
        // 假设您的 User 类就是 UserDetails 并且有 getId() 方法
        if (currentUser == null) {
            // 在实际应用中，如果认证失败，应该抛出异常或返回未授权
            // 为了 MVP，这里简单返回 null 或抛出非法状态异常
            throw new IllegalStateException("Authentication principal is null. User not authenticated.");
        }
        return currentUser.getId();
    }

    /**
     * 获取当前用户的学习日志
     * 只有学生可以访问自己的日志。
     * 教师/管理员可以通过 getAllLearningLogs 接口查询所有用户的日志。
     *
     * @param targetType 目标类型过滤 (e.g., "COURSE", "CHAPTER", "RESOURCE", "TASK", "QUESTION", "CONCEPT")
     * @param actionType 行为类型过滤 (e.g., "view", "click", "play", "answer", "download", "submit", "ask_dify")
     * @param startTime 开始时间 (格式: YYYY-MM-DDTHH:mm:ss)
     * @param endTime 结束时间 (格式: YYYY-MM-DDTHH:mm:ss)
     * @param page 页码 (默认为0)
     * @param size 每页大小 (默认为10)
     * @param currentUser 当前认证用户
     * @return 学习日志列表
     */
    @GetMapping("/my-logs")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')") // 任何角色都可以看自己的日志
    public Result<List<LearningLog>> getMyLearningLogs(
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) String actionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User currentUser
    ) {
        Long userId = getCurrentUserId(currentUser);
        List<LearningLog> logs = analysisService.getUserLearningLogs(
                userId, targetType, actionType, startTime, endTime, page, size);
        return Result.success( logs);
    }

    /**
     * 获取当前用户的学习日志总数
     * @param targetType 目标类型过滤
     * @param actionType 行为类型过滤
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param currentUser 当前认证用户
     * @return 总数
     */
    @GetMapping("/my-logs/count")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public Result<Long> countMyLearningLogs(
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) String actionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @AuthenticationPrincipal User currentUser
    ) {
        Long userId = getCurrentUserId(currentUser);
        Long count = analysisService.countUserLearningLogs(userId, targetType, actionType, startTime, endTime);
        return Result.success( count);
    }

    /**
     * 获取所有用户的学习日志（仅限教师/管理员）
     *
     * @param userId 可选，如果指定则查询特定用户的日志
     * @param targetType 目标类型过滤
     * @param actionType 行为类型过滤
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 页码
     * @param size 每页大小
     * @param currentUser 当前认证用户 (用于权限检查)
     * @return 学习日志列表
     */
    @GetMapping("/all-logs")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')") // 只有教师或管理员可以访问所有日志
    public Result<List<LearningLog>> getAllLearningLogs(
            @RequestParam(required = false) Long userId, // 允许管理员查询特定用户
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) String actionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal User currentUser
    ) {
        List<LearningLog> logs = analysisService.getAllLearningLogs(
                userId, targetType, actionType, startTime, endTime, page, size);
        return Result.success( logs);
    }

    /**
     * 获取所有用户的学习日志总数（仅限教师/管理员）
     * @param userId 可选，如果指定则查询特定用户的日志
     * @param currentUser 当前认证用户
     * @return 总数
     */
    @GetMapping("/all-logs/count")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public Result<Long> countAllLearningLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) String actionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @AuthenticationPrincipal User currentUser
    ) {
        Long count = analysisService.countAllLogs(userId, targetType, actionType, startTime, endTime);
        return Result.success( count);
    }


    /**
     * 获取当前用户的学习统计数据
     * @param startTime 统计开始时间
     * @param endTime 统计结束时间
     * @param currentUser 当前认证用户
     * @return 学习统计数据 DTO
     */
    @GetMapping("/my-stats")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public Result<LearningStatsDto> getMyLearningStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @AuthenticationPrincipal User currentUser
    ) {
        Long userId = getCurrentUserId(currentUser);
        LearningStatsDto stats = analyticsService.getUserLearningStats(userId, startTime, endTime);
        return Result.success( stats);
    }

    /**
     * 获取指定用户的学习统计数据（仅限教师/管理员）
     * @param userId 要查询的用户ID
     * @param startTime 统计开始时间
     * @param endTime 统计结束时间
     * @param currentUser 当前认证用户
     * @return 学习统计数据 DTO
     */
    @GetMapping("/user-stats/{userId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<LearningStatsDto> getUserLearningStats(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @AuthenticationPrincipal User currentUser
    ) {
        LearningStatsDto stats = analysisService.getUserLearningStats(userId, startTime, endTime);
        return Result.success( stats);
    }

    // TODO: 如果有 ConceptMasteryService 和相应的 Mapper，可以添加接口来获取概念掌握度数据
    // @GetMapping("/my-concept-mastery")
    // @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    // public ResponseEntity<List<UserConceptMastery>> getMyConceptMastery(@AuthenticationPrincipal User currentUser) {
    //     Long userId = getCurrentUserId(currentUser);
    //     List<UserConceptMastery> mastery = analyticsService.getUserConceptMastery(userId);
    //     return ResponseEntity.ok(mastery);
    // }

}
