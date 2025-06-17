package com.dd.ai_smart_course.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Log {
    private int id;
    private int userId;
    private String targetType; // course, chapter, concept, task
    private int targetId;
    private String actionType; // view, click, play, answer
    private Timestamp actionTime;
    private int duration; // 秒
    private String detail;  //detail: mysql中json类型变量


}
