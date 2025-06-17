package com.dd.ai_smart_course.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Course {
    private int id;
    private String name;
    private int teacherId;
    private String description;
    private String status;// published,draft,archived
    private Timestamp createdAt;
}
