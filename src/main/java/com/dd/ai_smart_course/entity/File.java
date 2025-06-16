package com.dd.ai_smart_course.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class File {
    private int id;
    private String name;
    private String fileUrl;
    private String fileType;
    private String ownerType; // course, chapter, task
    private int ownerId;
    private Timestamp uploadedAt;
}
