package com.dd.ai_smart_course.dto;

import com.dd.ai_smart_course.entity.Chapter;
import lombok.Data;


public class ChapterDTO extends Chapter {
    private String courseName;


    public ChapterDTO(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
