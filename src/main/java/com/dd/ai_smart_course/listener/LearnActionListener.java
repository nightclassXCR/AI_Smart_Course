package com.dd.ai_smart_course.listener;


import com.dd.ai_smart_course.entity.LearningLog;
import com.dd.ai_smart_course.event.LearningActionEvent;
import com.dd.ai_smart_course.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class LearnActionListener {
    @Autowired
    private LogMapper learningLogMapper;

    @EventListener
    @Async
    public void handleLearningActionEvent(LearningActionEvent event) {
        try {
            LearningLog log = new LearningLog();
            log.setUserId(event.getUserId());
            log.setTargetType(event.getTargetType());
            log.setTargetId(event.getTargetId());
            log.setActionType(event.getActionType());
            log.setActionTime(Timestamp.valueOf(LocalDateTime.now())); // 使用 actionTime 字段
            log.setDuration(event.getDuration());    // 设置 duration
            log.setDetail(event.getDetail());        // 设置 detail

            learningLogMapper.insertLearningLog(log);

            System.out.println("Learning Log successfully saved: " + log);
        } catch (Exception e) {
            System.err.println("Failed to save learning log: " + event.toString() + ". Error: " + e.getMessage());
        }
    }
}
