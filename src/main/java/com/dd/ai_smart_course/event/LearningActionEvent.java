package com.dd.ai_smart_course.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;


public class LearningActionEvent extends ApplicationEvent {
    private final int userId;
    private final String targetType; // <--- 从 TargetType 枚举改为 String
    private final int targetId;
    private final String actionType; // <--- 从 ActionType 枚举改为 String
    private Integer duration; // 新增字段
    private String detail;     // 新增字段

    public LearningActionEvent(Object source, int userId, String targetType, int targetId, String actionType, Integer duration, String detail) {
        super(source);
        this.userId = userId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.actionType = actionType;
        this.duration = duration;
        this.detail = detail;
    }

    public int getUserId() {
        return userId;
    }

    public String getTargetType() {
        return targetType;
    }

    public int getTargetId() {
        return targetId;
    }

    public String getActionType() {
        return actionType;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
