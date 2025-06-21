package com.dd.ai_smart_course.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Question {
    private int id;//问题ID
    private String context;//问题内容
    private QuestionDifficulty difficulty;//问题难度
    private Timestamp createdAt;//问题创建时间
    private Timestamp updatedAt;//问题更新时间
    private BigDecimal point; // 分数
    private int courseId;//所属课程ID
    private String answer;
    private int chapterId;//所属章节ID
    public enum QuestionDifficulty {
        easy, intermediate, advanced
    }

}

