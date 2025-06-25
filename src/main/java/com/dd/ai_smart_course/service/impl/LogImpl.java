package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.LearningLog;
import com.dd.ai_smart_course.mapper.LogMapper;
import com.dd.ai_smart_course.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public List<LearningLog> getAllLogs() {
        return logMapper.getAllLogs();
    }

    @Override
    public LearningLog getLogById(int id) {
        return logMapper.getLogById((long) id);
    }

    @Override
    public int addLog(LearningLog learnlog) {
        return logMapper.insertLearningLog(learnlog);
    }

    @Override
    public int updateLog(LearningLog learnlog) {
        return logMapper.updateLearningLog(learnlog);
    }

    @Override
    public int deleteLog(int id) {
        return logMapper.deleteLog(id);
    }

    @Override
    public List<LearningLog> findLearningLogs(Long userId, String targetType, String actionType, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate, Integer offset, Integer limit) {
        return logMapper.findLearningLogs(userId, targetType, actionType, startDate, endDate, offset, limit);
    }

    @Override
    public int countLearningLogs(Long userId, String targetType, String actionType, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) {
        return logMapper.countLearningLogs(userId, targetType, actionType, startDate, endDate);
    }
}
