package com.dd.ai_smart_course.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
}
