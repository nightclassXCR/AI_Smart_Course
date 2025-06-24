package com.dd.ai_smart_course.entity;


import lombok.Data;

@Data
public class Course_user {
    private int courseId;
    private int userId;
    private String role;

    public Course_user(Long courseId, Long userId, String student) {
    }
}
