package com.dd.ai_smart_course.service;

import com.dd.ai_smart_course.entity.Log;

import java.util.List;

public interface LogService {
    // 获取所有日志
    List<Log> getAllLogs();
    // 获取日志详情
    Log getLogById(int id);
    // 添加日志
    int addLog(Log log);
    // 更新日志信息
    int updateLog(Log log);
    // 删除日志
    int deleteLog(int id);
}
