package com.dd.ai_smart_course.entity;


import lombok.Data;

@Data
public class Question {
    private int id;
    private String content;
    private String type;// Single choice, Multiple choice,Short answer, Coding, essay
    private String difficulty;// Easy, Medium, Hard
    private String createdAt;
    private String updatedAt;
}
