package com.dd.ai_smart_course.entity;

import lombok.Data;

import java.sql.Timestamp;

//对应数据库关系resource
@Data
public class File {
    private int id;
    private String name;
    private String fileUrl;
    private String fileType;
    private String ownerType; // "COURSE", "CHAPTER" OR "TASK"
    private int ownerId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
