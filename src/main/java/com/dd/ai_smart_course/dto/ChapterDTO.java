package com.dd.ai_smart_course.dto;

import com.dd.ai_smart_course.entity.Chapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDTO  {
    private int id;
    private int courseId;
    private String title;
    private String content;
    private int sequence;
    private String createdAt;
    private String updatedAt;
    private String courseName;


}
