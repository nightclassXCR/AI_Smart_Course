package com.dd.ai_smart_course.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class QuestionDTO {
    private int id;
    private String content;
    private String difficulty;
    private Timestamp updatedAt;
    private Timestamp createdAt;
    private BigDecimal point;
    private int courseId;
    private String answer;
    private int  chapterId;
    private List<OptionDTO> options;

    // 问题关联的多个知识点概念
    private List<String> conceptNames;
    private List<Integer> conceptIds;
}
