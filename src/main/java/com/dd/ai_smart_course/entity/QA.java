package com.dd.ai_smart_course.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class QA {
    private int id;
    private int userId;
    private int courseId;
    private int conceptId;
    private String questionText;
    private String answerText;
    private String responderType; // ai, teacher, student
    private Timestamp createdAt;
}
