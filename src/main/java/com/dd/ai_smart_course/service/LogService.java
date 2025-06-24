package com.dd.ai_smart_course.service;

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
}
