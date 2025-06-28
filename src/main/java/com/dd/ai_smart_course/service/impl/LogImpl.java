package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.LearningLog;
import com.dd.ai_smart_course.mapper.LogMapper;
import com.dd.ai_smart_course.service.base.LogService;
import com.dd.ai_smart_course.service.exception.BusinessException;
import com.dd.ai_smart_course.service.exception.errorcode.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class LogImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private UserImpl userService;

    // 允许的 targetType 类型
    public static final List<String> ALLOWED_TARGET_TYPE = Arrays.asList("course", "chapter", "concept", "task");
    // 允许的 actionType 类型
    public static final List<String> ALLOWED_ACTION_TYPE = Arrays.asList("view", "click", "play", "answer");

    @Override
    public List<LearningLog> getAllLogs() {
        return logMapper.getAllLogs();
    }

    @Override
    public LearningLog getLogById(int id) {
        return logMapper.getLogById(id);
    }

    @Override
    public int addLog(LearningLog learnlog) throws BusinessException {
        try {
            checkFactor(learnlog);
            return logMapper.addLog(learnlog);
        } catch (BusinessException be) {
            log.error("can't add in \"learning_logs\" table: " + be.getMessage());
            throw be;
        } catch (Exception e) {
            log.error("can't add in \"learning_logs\" table: " + e.getMessage());
            throw new BusinessException(ErrorCode.FAILURE);
        }
    }

    @Override
    public int updateLog(LearningLog learnlog) throws BusinessException {
        try {
            checkFactor(learnlog);
            checkLogExists(learnlog.getId());
            return logMapper.updateLog(learnlog);
        } catch (BusinessException be) {
            log.error("can't update in \"learning_logs\" table: " + be.getMessage());
            throw be;
        } catch (Exception e) {
            log.error("can't update in \"learning_logs\" table: " + e.getMessage());
            throw new BusinessException(ErrorCode.FAILURE);
        }
    }

    @Override
    public int deleteLog(int id) {
        return logMapper.deleteLog(id);
    }

    @Override
    public List<LearningLog> findLearningLogs(int userId, String targetType, String actionType, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate, Integer offset, Integer limit) {
        return logMapper.findLearningLogs(userId, targetType, actionType, startDate, endDate, offset, limit);
    }

    @Override
    public int countLearningLogs(int userId, String targetType, String actionType, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) {
        return logMapper.countLearningLogs(userId, targetType, actionType, startDate, endDate);
    }

    // 校验 LearningLog 实体的关键字段
    @Override
    public void checkFactor(LearningLog log) throws BusinessException {

        // 校验 targetType
        if (log.getTargetType() == null) {
            throw new BusinessException(ErrorCode.LOG_TARGET_TYPE_NULL);
        }
        if(!ALLOWED_TARGET_TYPE.contains(log.getTargetType())){
            throw new BusinessException(ErrorCode.LOG_TARGET_TYPE_INVALID);
        }

        // 校验 actionType
        if(log.getActionType() == null){
            throw new BusinessException(ErrorCode.LOG_ACTION_TYPE_NULL);
        }
        if(!ALLOWED_ACTION_TYPE.contains(log.getActionType())){
            throw new BusinessException(ErrorCode.LOG_ACTION_TYPE_INVALID);
        }

        // 检验userID
        userService.checkUserExists(log.getUserId());
    }

    // 检查日志是否存在
    @Override
    public void checkLogExists(int logId) throws BusinessException {
        LearningLog existing = logMapper.getLogById(logId);
        if (existing == null) {
            throw new BusinessException(ErrorCode.LOG_NOT_EXISTS);
        }
    }
}
