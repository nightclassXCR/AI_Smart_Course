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

    public QA(){

    }

    public QA(int id, int userId, int courseId, int conceptId, String questionText, String answerText, String responderType, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.conceptId = conceptId;
        this.questionText = questionText;
        this.answerText = answerText;
        this.responderType = responderType;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getConceptId() {
        return conceptId;
    }

    public void setConceptId(int conceptId) {
        this.conceptId = conceptId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getResponderType() {
        return responderType;
    }

    public void setResponderType(String responderType) {
        this.responderType = responderType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
