package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.LearningLog;
import com.dd.ai_smart_course.mapper.LogMapper;
import com.dd.ai_smart_course.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LogImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public List<LearningLog> getAllLogs() {
        return logMapper.getAllLogs();
    }

    @Override
    public LearningLog getLogById(int id) {
        return logMapper.getLogById(id);
    }

    @Override
    public int addLog(LearningLog learnlog) {
        return logMapper.addLog(learnlog);
    }

    @Override
    public int updateLog(LearningLog learnlog) {
        return logMapper.updateLog(learnlog);
    }

    @Override
    public int deleteLog(int id) {
        return logMapper.deleteLog(id);
    }
}
