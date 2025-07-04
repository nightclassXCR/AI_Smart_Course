package com.dd.ai_smart_course.dto;

import lombok.Data;

import java.math.BigDecimal;

//用户答完题之后的结果
@Data
public class TaskAnswerResultDTO {
    private int taskId;
    private int userId;
    private int questionId;
    private String userAnswer;
    private boolean isCorrect;
    private String correctAnswer;
    private BigDecimal point;
}
