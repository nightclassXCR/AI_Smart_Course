package com.dd.ai_smart_course.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConceptMasteryDTO {
    private int userId;
    private String username; // 添加用户名
    private int conceptId;
    private String conceptName; // 添加概念名称
    private Integer masteryLevel;
    private LocalDateTime lastUpdated;
}
