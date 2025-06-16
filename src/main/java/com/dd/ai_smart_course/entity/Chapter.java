package com.dd.ai_smart_course.entity;


import lombok.Data;

@Data
public class Chapter {
    private int id;
    private int courseId;
    private String title;
    private String content;
    private int sortOrder;
}
