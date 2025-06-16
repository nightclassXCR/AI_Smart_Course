package com.dd.ai_smart_course.entity;


import lombok.Data;

@Data
public class Concept {
    private int id;
    private int chapterId;
    private String name;
    private String description;
}
