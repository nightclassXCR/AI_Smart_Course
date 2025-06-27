package com.dd.ai_smart_course.entity;


import lombok.Data;

@Data
public class Task_question {
    private int task_id;
    private int question_id;
    private double max_score;
    private int sequence;

}