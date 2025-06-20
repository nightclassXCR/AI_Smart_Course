package com.dd.ai_smart_course.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class LearningLog {
    private long id;
    private long userId;
    private String targetType; // course, chapter, concept, task
    private long targetId;
    private String actionType; // view, click, play, answer
    private Timestamp actionTime;
    private int duration; // 秒
    private String detail;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Timestamp getActionTime() {
        return actionTime;
    }

    public void setActionTime(Timestamp actionTime) {
        this.actionTime = actionTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setDetail(String detail) {
    }
    public String getDetail() {
        return detail;
    }
}
