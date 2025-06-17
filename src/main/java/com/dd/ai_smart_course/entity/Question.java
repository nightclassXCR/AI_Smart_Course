package com.dd.ai_smart_course.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Question {
    private int id;//问题ID
    private String context;//问题内容
    private String type;//问题类型
    private String difficulty;//问题难度
    private Timestamp created_at;//问题创建时间
    private Timestamp updated_at;//问题更新时间
}
