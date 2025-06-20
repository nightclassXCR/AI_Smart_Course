package com.dd.ai_smart_course.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class QuestionDTO {
    private String context;
    private String type;        // choice / fill / essay
    private String difficulty;  // easy / intermediate / advanced
    private BigDecimal point;
    private List<OptionDTO> options;
}
