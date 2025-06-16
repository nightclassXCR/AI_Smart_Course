package com.dd.ai_smart_course.entity;


import lombok.Data;

@Data
public class Concept {
    private int id;
    private int chapterId;
    private String name;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
