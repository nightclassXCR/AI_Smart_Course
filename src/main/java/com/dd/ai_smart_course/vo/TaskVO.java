package com.dd.ai_smart_course.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskVO {

    private String title;
    private String courseName;
    private LocalDateTime deadline;
}
