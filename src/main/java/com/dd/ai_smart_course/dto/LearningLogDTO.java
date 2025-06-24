package com.dd.ai_smart_course.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LearningLogDTO {
    private Long id;
    private Long userId;
    private String username; // 添加用户名，方便前端展示
    private String targetType;
    private Long targetId;
    private String actionType;
    private LocalDateTime actionTime;
    private Integer duration;
    private String detail;
}
