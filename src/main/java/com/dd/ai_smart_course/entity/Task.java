package com.dd.ai_smart_course.entity;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class Task {
    private Integer id;

    private Integer courseId;

    private String title;

    private Type type;

    private String description;

    private Status status;

    private LocalDateTime deadline;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Getters and Setters
    public enum Type {
        reading, homework, project, quiz, exam
    }

    public enum Status {
        draft, published, completed
    }
}
