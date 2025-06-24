package com.dd.ai_smart_course.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LearningStatsDTO {
    private Long userId;
    private String username;
    private Long totalStudyTime; // 总学习时长（秒）
    private Integer totalActions; // 总操作次数
    private Integer completedChapters; // 完成章节数
    private Integer completedConcepts; // 完成概念数
    private Double averageScore; // 平均分数
    private LocalDateTime lastActivityTime; // 最后活动时间
    private Integer activeDays; // 活跃天数
}
