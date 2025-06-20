package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.Log;
import com.dd.ai_smart_course.mapper.LogMapper;
import com.dd.ai_smart_course.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LogImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public List<Log> getAllLogs() {
        return logMapper.getAllLogs();
    }

    @Override
    public Log getLogById(int id) {
        return logMapper.getLogById(id);
    }

    @Override
    public int addLog(Log log) {
        return logMapper.addLog(log);
    }

    @Override
    public int updateLog(Log log) {
        return logMapper.updateLog(log);
    }

    @Override
    public int deleteLog(int id) {
        return logMapper.deleteLog(id);
    }
}
