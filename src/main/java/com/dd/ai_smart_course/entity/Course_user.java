package com.dd.ai_smart_course.entity;

import lombok.Data;

@Data
public class Course_user {
    private int courseId;
    private int userId;
    private String role;

    public Course_user() {}

    public Course_user(Long courseId, Long userId, String role) {
        this.courseId = courseId.intValue();
        this.userId = userId.intValue();
        this.role = role;
    }

    public Course_user(int courseId, int userId, String role) {
        this.courseId = courseId;
        this.userId = userId;
        this.role = role;
    }
}
