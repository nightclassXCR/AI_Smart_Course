package com.dd.ai_smart_course.entity;

import lombok.Data;

@Data
public class Option {
    private int id;
    private int question_id;//问题id
    private String opt_Key; //A, B, C, D
    private String opt_Value;//选项内容
}