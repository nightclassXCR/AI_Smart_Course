package com.dd.ai_smart_course.Entity;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Score {
    private int id;
    private int userId;
    private int taskId;
    private BigDecimal grade; // 用BigDecimal存储浮点分数
    private String comment;
    private Timestamp submitTime;
}
