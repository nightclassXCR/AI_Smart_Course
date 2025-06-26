package com.dd.ai_smart_course.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class QA {
    private long id;
    private long userId;
    private long courseId;
    private long conceptId;
    private long responderId;
    private String questionText;
    private String answerText;
    private String responderType; // ai, teacher, student
    private String status;
    private Timestamp createdAt;
    private Timestamp answeredAt;
    private boolean rating;

}
