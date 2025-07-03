package com.dd.ai_smart_course.dto;

import lombok.Data;

import java.util.List;

@Data
public class ScoreDTO {
    private Integer studentId;
    private Integer courseId;
    private Integer finalScore;  //课程最终分数
}
