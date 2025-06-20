package com.dd.ai_smart_course.entity;

import lombok.Data;

@Data
public class QuestionOption {
    private int id;
    private int question_id;
    private String opt_Key;
    private String opt_Value;
}
