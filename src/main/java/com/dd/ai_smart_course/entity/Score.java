package com.dd.ai_smart_course.entity;

import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.apache.catalina.util.Introspection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Score {

    private Integer id;

    private Integer userId;

    private Integer taskId;

    private BigDecimal totalScore;

    private BigDecimal aiScore;

    private BigDecimal teacherScore;

    private BigDecimal finalScore;

    private String comment;

    private LocalDateTime submittedAt;

    private LocalDateTime gradedAt;

    private GradingMethod gradingMethod;

    // Getters and Setters
    public enum GradingMethod {
        ai, teacher, mixed
    }
}
