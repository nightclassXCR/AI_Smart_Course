package com.dd.ai_smart_course.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScoreDTO {
    private Integer studentId;
    private Integer courseId;
    private BigDecimal finalScore;  //课程最终分数
}
