package com.dd.ai_smart_course.entity;

import lombok.Data;

@Data
public class Option {
    private int id;
    private int question_id;//问题id
    private String optKey; //A, B, C, D
    private String optValue;//选项内容
}