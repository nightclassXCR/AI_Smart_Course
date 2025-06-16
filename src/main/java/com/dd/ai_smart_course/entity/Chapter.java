package com.dd.ai_smart_course.entity;


import lombok.Data;

@Data
public class Chapter {
    private int id;
    private int courseId;
    private String title;
    private String content;
    private int sortOrder;

    public Chapter(int id, int courseId, String title, String content, int sortOrder) {
    }

    public Chapter() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
