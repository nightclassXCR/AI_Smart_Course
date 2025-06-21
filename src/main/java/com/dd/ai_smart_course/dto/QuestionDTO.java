package com.dd.ai_smart_course.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class QuestionDTO {
    private int id;
    private String content;
    private String  difficulty;
    private String updatedAt;
    private String createdAt;
    private BigDecimal point;
    private int courseId;
    private String answer;
    private int  chapterId;
    private List<OptionDTO> options;
}
