package com.dd.ai_smart_course.service.base;

import com.dd.ai_smart_course.entity.LearningLog;

import java.util.List;

public interface LogService {
    // 获取所有日志
    List<LearningLog> getAllLogs();
    // 获取日志详情
    LearningLog getLogById(int id);
    // 添加日志
    int addLog(LearningLog learnlog);
    // 更新日志信息
    int updateLog(LearningLog learnlog);
    // 删除日志
    int deleteLog(int id);

    /**
     * 根据条件查询学习日志记录。
     * @param userId     用户ID (可选)
     * @param targetType 目标类型 (可选)
     * @param actionType 动作类型 (可选)
     * @param startDate  开始时间 (可选)
     * @param endDate    结束时间 (可选)
     * @param offset     分页偏移量 (可选)
     * @param limit      分页限制数量 (可选)
     * @return 学习日志列表
     */
    List<LearningLog> findLearningLogs(Long userId, String targetType, String actionType, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate, Integer offset, Integer limit);

    /**
     * 根据条件统计学习日志记录总数，用于分页。
     * @param userId     用户ID (可选)
     * @param targetType 目标类型 (可选)
     * @param actionType 动作类型 (可选)
     * @param startDate  开始时间 (可选)
     * @param endDate    结束时间 (可选)
     * @return 学习日志总数
     */
    int countLearningLogs(Long userId, String targetType, String actionType, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
}
